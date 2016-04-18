package com.mf.shop

import scala.concurrent.duration.FiniteDuration
import scala.concurrent.duration.SECONDS
import org.scalatest._
import com.mf.shop.service.CartPricer
import com.mf.shop.domain._

class CartPricerSpec extends WordSpecLike 
  with Matchers
{

  val priceListConfig :Map[String, Set[String]] = Map("Orange" -> Set("1x0.25","3x0.5"), "Apple" -> Set("1x0.6","2x0.6"))
  
   val priceList :PriceList = new PriceList( priceListConfig.mapValues { x => CartCheckoutApp.createOffersFromStrings(x) })
  
  "CartPricer" should {
  
    "price empty Cart to Zero" in {
       val pricedCart = CartPricer.checkOutCart(new Cart(Array[String]()), priceList)
       assert(pricedCart.totalPrice == 0)
    }
  
    "price two oranges and one apple" in {
       val pricedCart = CartPricer.checkOutCart(new Cart(Array("Orange","Apple","Orange")), priceList)
       assert(pricedCart.totalPrice == 1.1)
    }
  
   "price to zero unknown items (one Pear) and remove it from output Cart" in {
     val pricedCart = CartPricer.checkOutCart(new Cart(Array("Pear","Orange","Orange")), CartCheckoutApp.loadPrices())
       assert(pricedCart.totalPrice == 0.5)
       assert(pricedCart.items.size == 1 )
    }

   "price two oranges and one apple from config price source" in {
       val pricedCart = CartPricer.checkOutCart(new Cart(Array("Orange","Apple","Orange")), priceList)
       assert(pricedCart.totalPrice == 1.1)
    }
   
    "price use buy one get one free on apples " in {
      val pricedCart = CartPricer.checkOutCart(new Cart(Array("Apple","Apple")), priceList)
       assert(pricedCart.totalPrice == 0.6)
       val pricedCart2 = CartPricer.checkOutCart(new Cart(Array("Apple","Apple","Apple")), priceList)
       assert(pricedCart2.totalPrice == 1.2)
    }
    
    "price use 3 for price of 2 on oranges " in {
       val pricedCart = CartPricer.checkOutCart(new Cart(Array("Orange","Orange")), priceList)
       assert(pricedCart.totalPrice == 0.5)
       val pricedCart2 = CartPricer.checkOutCart(new Cart(Array("Orange","Orange","Orange")), priceList)
       assert(pricedCart2.totalPrice == 0.5)
    }
  
  }
}


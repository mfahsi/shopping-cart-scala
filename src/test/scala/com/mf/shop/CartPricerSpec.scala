package com.mf.shop

import scala.concurrent.duration.FiniteDuration
import scala.concurrent.duration.SECONDS
import org.scalatest._
import com.mf.shop.service.CartPricer
import com.mf.shop.domain._

class CartPricerSpec extends WordSpecLike 
  with Matchers
{
  
  val priceList :Map[String,String] = Map("Orange" -> "0.25", "Apple" -> "0.6")
  
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
  
  }
}


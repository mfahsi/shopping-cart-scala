package com.mf.shop.service
import scala.collection.SortedSet
import com.mf.shop.domain._
import com.mf.shop.domain.CartItemWithCheckedPrice

trait CartPricer {

  def checkOutCart(cart: Cart, priceList: PriceList): PricedCart = {

    def applyPricesToCartItems(cart: Cart, priceList: PriceList): Set[CartItemWithCheckedPrice] =
      {
        cart.items.map(item => priceList.offers get item.itemId match {
          case None       => new NotPricedCartItem(item, "price not found in list")
          case Some(list) => ItemPricer.priceOneItemWithChoiceOfOffers(item, list.toList) 
        })
        
      }

    //price returned is for PricedElements, NotPricedCartItem will have to be excluded from Cart
    def checkOutPricedItems(items: Set[PricedCartItem]): PricedCart =
      {
        new PricedCart(items, items.foldLeft(0.0)(_ + _.price))
      }

    val pricedElements = applyPricesToCartItems(cart, priceList)

    //we assume all prices are available as config has Apple and Orange
    //otherwise we filter items if we can't find price for them (raise error message)
    if (pricedElements.exists { x => x.isInstanceOf[NotPricedCartItem] }) {
      Console.println("Some items were removed from cart due to unavailability of price")
    }

    val pricedCart = checkOutPricedItems(pricedElements
      .filter(x => x.isInstanceOf[PricedCartItem])
      .map(x => x.asInstanceOf[PricedCartItem]))
    pricedCart

  }

}

object CartPricer extends CartPricer
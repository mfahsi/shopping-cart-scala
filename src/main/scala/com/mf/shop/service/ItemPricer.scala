package com.mf.shop.service

import scala.annotation.tailrec

import com.mf.shop.domain.CartItem
import com.mf.shop.domain.ItemOffer
import com.mf.shop.domain.PricedCartItem

trait ItemPricer {
  
    def priceOneItemWithChoiceOfOffers(item: CartItem, offers: List[ItemOffer]) = {

      new PricedCartItem(item, priceOneItemWithChoiceOfOffersRec(item.quantity, offers.sortBy { x => x.quantity }.reverse, 0))
    }
    
    @tailrec
    final def priceOneItemWithChoiceOfOffersRec(remaining: Int, offers: List[ItemOffer], price: Double): Double = {
      //assume best offer for large quantity
      if (remaining == 0) {
        price
      } else {
        offers match {
          case Nil => throw new IllegalArgumentException("missing unitary price?")
          case bestoffer :: tail => {
            val applicableQuantity = (remaining / bestoffer.quantity)
            tail match {
              case Nil =>  price + applicableQuantity * bestoffer.price
              case _   => priceOneItemWithChoiceOfOffersRec(remaining - applicableQuantity * bestoffer.quantity, tail, price + applicableQuantity * bestoffer.price)
            }
         
          }
        }
      }
    }
}

object ItemPricer extends ItemPricer
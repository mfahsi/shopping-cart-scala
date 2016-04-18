package com.mf.shop.domain

import scala.collection.SortedSet

case class ItemOffer(val quantity: Integer, val price: Double)

object ItemOffer {
  
  def fromString(stringRepresentation: String): Option[ItemOffer] = {
    val array = stringRepresentation.split("x")
    array match {
      case Array(quantity, price) => Some(new ItemOffer(quantity.toInt, price.toDouble))
      case _                      => None
    }

  }
} 


case class PriceList(val offers : Map[String, Set[ItemOffer]])

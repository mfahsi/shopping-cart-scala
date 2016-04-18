package com.mf.shop.domain

 /** Carts Items workflow : string -> CartItem --> CartItemWithCheckedPrice **/ 
 case class CartItem(val itemId : String, val quantity : Integer ){
       override def toString =  itemId +" x "+quantity 
 }
 
 abstract trait CartItemWithCheckedPrice //either priced or failed to price
 case class PricedCartItem(val item : CartItem, val price : Double) extends CartItemWithCheckedPrice
 {
      override def toString = item +" -> "+price 
 }
 case class NotPricedCartItem(val item : CartItem, val cause : String) extends CartItemWithCheckedPrice
    
 
/** Basket / Cart Types **/
 class Basket[T](val items : Set[T])
 
 //A Cart have one instance of CartItem per product that has total quantity
 class Cart(override val items : Set[CartItem]) extends Basket[CartItem](items){
      //build Cart from strings : Apple, Orange, Orange becomes Apple->1, Orange->2 
      def this(stringElements : Array[String]) =  this(
            stringElements
            .groupBy( x => x )
            .mapValues[CartItem](x=> new CartItem(x.head,x.length))
            .values.toSet[CartItem]
            )
 }
    
 class PricedCart(override val items : Set[PricedCartItem], val totalPrice : Double) extends Basket[PricedCartItem](items)
 {
     override def toString = "PricedCart total="+totalPrice +" for items {"+items.mkString(",")+"}" 
 }
  
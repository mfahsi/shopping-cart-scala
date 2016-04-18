package com.mf.shop

import java.util.Map.Entry
import scala.collection.JavaConverters.asScalaBufferConverter
import scala.collection.JavaConverters.asScalaSetConverter
import com.mf.shop.domain.Cart
import com.mf.shop.service.CartPricer
import com.typesafe.config.ConfigFactory
import com.typesafe.config.ConfigObject
import com.typesafe.config.ConfigValue
import com.mf.shop.domain._

object CartCheckoutApp extends App {
  
  def loadPrices() : PriceList = 
  {
     /*val config = ConfigFactory.load()
     val list : Iterable[ConfigObject] = config.getObjectList("priceList").asScala
    (for {
      item : ConfigObject <- list
      entry : Entry[String, ConfigValue] <- item.entrySet().asScala
      key = entry.getKey
      uri = Set(entry.getValue.unwrapped().toString())
    } yield (key, uri)).toMap*/
    
    //just use hard coded list
   val prices = Map("Orange" -> Set("1x0.25","3x0.5"), "Apple" -> Set("1x0.6","2x0.6"))
    .mapValues( offers => createOffersFromStrings(offers)) 
    
    new PriceList(prices)
  }
  
   lazy val priceList : PriceList = loadPrices()
   
   def createOffersFromStrings(strings : Set[String]) : Set[ItemOffer] ={
     strings.map( x => ItemOffer.fromString(x).get)
   }
  
    Console.println("started with prices : "+priceList)
    Console.println("elements in your Cart : "+args.mkString(","))
    
    val pricedCart = CartPricer.checkOutCart(new Cart(args), priceList)
    
    Console.println("Output : "+pricedCart)
   
}
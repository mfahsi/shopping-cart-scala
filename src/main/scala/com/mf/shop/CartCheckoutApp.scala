package com.mf.shop

import java.util.Map.Entry

import scala.collection.JavaConverters.asScalaBufferConverter
import scala.collection.JavaConverters.asScalaSetConverter

import com.mf.shop.domain.Cart
import com.mf.shop.service.CartPricer
import com.typesafe.config.ConfigFactory
import com.typesafe.config.ConfigObject
import com.typesafe.config.ConfigValue

object CartCheckoutApp extends App {
  
  def loadPrices() : Map[String, String] = 
  {
     val config = ConfigFactory.load()
     val list : Iterable[ConfigObject] = config.getObjectList("priceList").asScala
    (for {
      item : ConfigObject <- list
      entry : Entry[String, ConfigValue] <- item.entrySet().asScala
      key = entry.getKey
      uri = entry.getValue.unwrapped().toString()
    } yield (key, uri)).toMap
  }
  
   lazy val priceList : Map[String, String] = loadPrices()
   
  
    Console.println("started with prices : "+priceList)
    Console.println("elements in your Cart : "+args.mkString(","))
    
    val pricedCart = CartPricer.checkOutCart(new Cart(args), priceList)
    
    Console.println("Output : "+pricedCart)
   
}
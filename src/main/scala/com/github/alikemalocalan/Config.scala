package com.github.alikemalocalan

import com.typesafe.config

trait Config {

  import com.typesafe.config.ConfigFactory

  val conf: config.Config = ConfigFactory.load()
  val twitterTokenConfig: config.Config = conf.getConfig("twitter.tokens")
  val trelloTokenConfig: config.Config = conf.getConfig("trello.tokens")
  val apiConfig: config.Config = conf.getConfig("api")

  val consumerKey: String = twitterTokenConfig.getString("consumerKey")
  val consumerSecret: String = twitterTokenConfig.getString("consumerSecret")
  val accessToken: String = twitterTokenConfig.getString("accessToken")
  val accessTokenSecret: String = twitterTokenConfig.getString("accessTokenSecret")

  /*
 https://trello.com/1/authorize?expiration=never&scope=read,write,account&response_type=token&name=Server%20Token&key=${trelloKey}
  */
  val trelloAccessToken: String = trelloTokenConfig.getString("accessToken")
  val trelloAccessSecret: String = trelloTokenConfig.getString("accessTokenSecret")

  val address: String = apiConfig.getString("host")
  val port: Int = apiConfig.getInt("port")

}

package com.github.alikemalocalan

import com.typesafe.config

trait Config {

  import com.typesafe.config.ConfigFactory

  val conf: config.Config = ConfigFactory.load()
  val tokenConfig: config.Config = conf.getConfig("tokens")
  val apiConfig: config.Config = conf.getConfig("api")

  val consumerKey: String = tokenConfig.getString("consumerKey")
  val consumerSecret: String = tokenConfig.getString("consumerSecret")
  val accessToken: String = tokenConfig.getString("accessToken")
  val accessTokenSecret: String = tokenConfig.getString("accessTokenSecret")

  val address: String = apiConfig.getString("host")
  val port: Int = apiConfig.getInt("port")

}

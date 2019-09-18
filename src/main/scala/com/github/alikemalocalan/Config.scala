package com.github.alikemalocalan

import com.typesafe.config

trait Config {
  import com.typesafe.config.ConfigFactory

  val conf: config.Config = ConfigFactory.load();
  val apiConfig: config.Config = conf.getConfig("api")

  val consumerKey: String = apiConfig.getString("consumerKey")
  val consumerSecret: String =apiConfig.getString("consumerSecret")
  val accessToken: String =apiConfig.getString("accessToken")
  val accessTokenSecret: String =apiConfig.getString("accessTokenSecret")

}

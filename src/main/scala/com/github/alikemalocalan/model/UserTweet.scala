package com.github.alikemalocalan.model

import spray.json.{DefaultJsonProtocol, RootJsonFormat}

case class UserTweet(text: Option[String], urls: ExternalURl)

object UserTweetProtocol extends DefaultJsonProtocol {

  import ExternalURlProtocol._

  implicit val userTweetJsonFormat: RootJsonFormat[UserTweet] = jsonFormat2(UserTweet)
}
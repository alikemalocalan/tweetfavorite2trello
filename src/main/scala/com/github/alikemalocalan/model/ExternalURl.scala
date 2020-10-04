package com.github.alikemalocalan.model

import spray.json.{DefaultJsonProtocol, RootJsonFormat}

case class ExternalURl(expanded_url: Array[String], imageUrls: Array[String], videoUrl: Option[String] = None)

object ExternalURlProtocol extends DefaultJsonProtocol {
  implicit val externalUrlJsonFormat: RootJsonFormat[ExternalURl] = jsonFormat3(ExternalURl)
}

case class User(id: Long, screenName: String)

object UserProtocol extends DefaultJsonProtocol {
  implicit val userJsonFormat: RootJsonFormat[User] = jsonFormat2(User)
}
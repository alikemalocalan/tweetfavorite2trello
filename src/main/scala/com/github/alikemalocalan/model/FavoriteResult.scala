package com.github.alikemalocalan.model

import spray.json.{DefaultJsonProtocol, RootJsonFormat}

case class FavoriteResult(size: Int, tweets: List[UserTweet])

object FavoriteResultProtocol extends DefaultJsonProtocol {

  import UserTweetProtocol._

  implicit val favoriteResultJsonFormat: RootJsonFormat[FavoriteResult] = jsonFormat2(FavoriteResult)
}

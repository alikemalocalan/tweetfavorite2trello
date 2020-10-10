package com.github.alikemalocalan.controllers

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.headers.RawHeader
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.github.alikemalocalan.model.UserTweet
import com.github.alikemalocalan.services.TwitterServices

object TweetController {
  /*
 https://trello.com/1/authorize?expiration=never&scope=read,write,account&response_type=token&name=Server%20Token&key=${trelloKey}&return_url
  */

  val tweetsRoute: Route = path("twitter" / "tweets") {
    pathEndOrSingleSlash {
      get {
        parameters(Symbol("page").as[Int].?, Symbol("count").as[Int].?) { (page, count) =>
          import com.github.alikemalocalan.model.UserTweetProtocol._

          val pageNumber = page.getOrElse(1)
          val countNumber = count.getOrElse(50)
          val result: List[UserTweet] = TwitterServices.getFavoritesWithPagination(page = pageNumber, maxTweetNumber = countNumber)
          val tweetNumberHeader = RawHeader("x-array-size", result.length.toString)
          respondWithHeaders(tweetNumberHeader) {
            complete(result)
          }
        }
      }
    }
  }
}

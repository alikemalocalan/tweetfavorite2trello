package com.github.alikemalocalan.services

import com.github.alikemalocalan.Config
import com.github.alikemalocalan.model.{ExternalURl, FavoriteResult, User, UserTweet}
import twitter4j.conf.ConfigurationBuilder
import twitter4j.{Paging, Twitter, TwitterFactory}

import scala.jdk.CollectionConverters._

object TwitterServices extends Config {

  val cb: ConfigurationBuilder = new ConfigurationBuilder()
    .setDebugEnabled(true)
    .setOAuthConsumerKey(consumerKey)
    .setOAuthConsumerSecret(consumerSecret)
    .setOAuthAccessToken(accessToken)
    .setOAuthAccessTokenSecret(accessTokenSecret)
  val tf = new TwitterFactory(cb.build())
  val twitter: Twitter = tf.getInstance()

  def getFavoritesWithPagination(username: String = "akemalocalan", page: Int, maxTweetNumber: Int): FavoriteResult = {

    @scala.annotation.tailrec
    def get(username: String, currentPage: Int, maxTweetNumber: Int, result: List[UserTweet] = List()): List[UserTweet] = {
      if (currentPage <= page) {
        val onePage: List[UserTweet] = twitter.getFavorites(username, new Paging(currentPage, maxTweetNumber)).asScala.toList
          .map { tweet =>
            val imageUrls: Array[String] = tweet.getMediaEntities.filter(_.getType == "photo").map(_.getMediaURLHttps)
            val videoUrls: Option[String] = tweet.getMediaEntities.filter(_.getType == "video").map(_.getVideoVariants.maxBy(_.getBitrate).getUrl).headOption
            val externalUrl: Array[String] = tweet.getURLEntities.map(_.getExpandedURL)
            val user = User(tweet.getUser.getId, tweet.getUser.getScreenName)
            UserTweet(tweet.getId, tweet.getCreatedAt, Option(tweet.getText), ExternalURl(externalUrl, imageUrls, videoUrls), user)
          }
        Thread.sleep(100)
        get(username, currentPage + 1, maxTweetNumber, onePage ++ result)
      }
      else result
    }

    val result = get(username, 1, maxTweetNumber)
    FavoriteResult(result.length, result)
  }
}
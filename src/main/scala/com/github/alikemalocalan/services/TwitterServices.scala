package com.github.alikemalocalan.services

import com.github.alikemalocalan.Config
import org.apache.commons.logging.{Log, LogFactory}
import twitter4j.conf.ConfigurationBuilder
import twitter4j.{Paging, Twitter, TwitterFactory}

import scala.jdk.CollectionConverters._

case class ExternalURl(expanded_url: Array[String], imageUrls: Array[String], videoUrl: Option[String] = None)

case class UserTweet(text: Option[String], urls: ExternalURl)

case class FavoriteResult(size: Int, tweets: List[UserTweet])

object TwitterServices extends Config{
  val logger: Log = LogFactory.getLog(this.getClass)

  val cb: ConfigurationBuilder = new ConfigurationBuilder()
    .setDebugEnabled(true)
    .setOAuthConsumerKey(consumerKey)
    .setOAuthConsumerSecret(consumerSecret)
    .setOAuthAccessToken(accessToken)
    .setOAuthAccessTokenSecret(accessTokenSecret)
  val tf = new TwitterFactory(cb.build())
  val twitter: Twitter = tf.getInstance()

  def getFavorites(username: String = "alikemalocalan", page: Int, maxTweetNumber: Int): FavoriteResult = {
    val result = getFavoritesWithPagination(username, page, maxTweetNumber)
    FavoriteResult(result.length, result)
  }

  def getFavoritesWithPagination(username: String, page: Int, maxTweetNumber: Int): List[UserTweet] = {

    @scala.annotation.tailrec
    def getFavorites(username: String, currentPage: Int, maxTweetNumber: Int, result: List[UserTweet] = List()): List[UserTweet] = {
      if (currentPage <= page) {
        val onePage: List[UserTweet] = twitter.getFavorites(username, new Paging(currentPage, maxTweetNumber)).asScala.toList
          .map { x =>
            val imageUrls: Array[String] = x.getMediaEntities.filter(_.getType == "photo").map(_.getMediaURLHttps)
            val videoUrls: Option[String] = x.getMediaEntities.filter(_.getType == "video").map(_.getVideoVariants.maxBy(_.getBitrate).getUrl).headOption
            val externalUrl: Array[String] = x.getURLEntities.map(_.getExpandedURL)
            UserTweet(Option(x.getText), ExternalURl(externalUrl, imageUrls, videoUrls))
          }
        Thread.sleep(100)
        getFavorites(username, currentPage + 1, maxTweetNumber, onePage ++ result)
      }
      else result
    }

    getFavorites(username, 1, maxTweetNumber)
  }
}
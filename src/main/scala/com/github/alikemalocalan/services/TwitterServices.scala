package com.github.alikemalocalan.services

import com.github.alikemalocalan.Config
import org.apache.commons.logging.{Log, LogFactory}
import twitter4j.conf.ConfigurationBuilder
import twitter4j.{Paging, Twitter, TwitterFactory}

import scala.collection.JavaConversions._


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

  def getFavorites(username: String = "alikemalocalan"): FavoriteResult = {
    val result = getFavoritesWithPagination(username)
    FavoriteResult(result.length, result)
  }

  def getFavoritesWithPagination(username: String = "alikemalocalan", maxTweetNumber: Int = 50): List[UserTweet] =
    twitter.getFavorites(username, new Paging(1, maxTweetNumber)).toList
      .map { x =>
        val imageUrls: Array[String] = x.getMediaEntities.filter(_.getType == "photo").map(_.getMediaURLHttps)
        val videoUrls: Option[String] = x.getMediaEntities.filter(_.getType == "video").map(_.getVideoVariants.maxBy(_.getBitrate).getUrl).headOption
        val externalUrl: Array[String] = x.getURLEntities.map(_.getExpandedURL)
        UserTweet(Option(x.getText), ExternalURl(externalUrl, imageUrls, videoUrls))
      }
}
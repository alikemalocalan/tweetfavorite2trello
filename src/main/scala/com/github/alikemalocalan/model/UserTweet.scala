package com.github.alikemalocalan.model


import java.util.Date

import com.github.alikemalocalan.util.Utils._
import com.julienvey.trello.domain.Card
import net.steppschuh.markdowngenerator.image.Image
import net.steppschuh.markdowngenerator.link.Link
import net.steppschuh.markdowngenerator.text.Text
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

case class UserTweet(tweetID: Long, createdAt: Date, text: Option[String], urls: ExternalURl, user: User) {
  def getTitle = s"${tweetID} ${user.screenName} ${text.map(_.substring(0, text.filter(_.length < 30).map(_.length).getOrElse(30))).getOrElse("")}"

  def getTweetUrl = s"https://twitter.com/${user.screenName}/status/${tweetID}"

  def embededVideo: Option[String] = urls.videoUrl.map(video => //TODO  fix it
    s"""<iframe width="560" height="315" src="$video" frameborder="0" allowfullscreen>video_${tweetID}</iframe>"""
  )

  def description: String = {
    val tweetUrl = new Link("Tweet Url", getTweetUrl)
    val pictureUrls = urls.imageUrls.zipWithIndex.map { case (pic, index) => new Image(s"${tweetID}_${index + 1}", pic).toString }
    val extraLink: String = urls.expanded_url.map(link => new Link(link, link).toString).mkString("\n")

    val sb = new StringBuilder()

    if (text.nonEmpty) {
      sb.append(new Text(text.getOrElse(""))).append("\n")
        .append("\n")
    }

    sb.append(tweetUrl).append("\n")

    if (extraLink.nonEmpty) {
      sb.append(extraLink).append("\n")
    }

    if (pictureUrls.nonEmpty) {
      pictureUrls.foreach(picUrl => sb.append(picUrl).append("\n"))
      sb.append("\n")
    }

    if (embededVideo.nonEmpty) {
      embededVideo.foreach(picUrl => sb.append(picUrl).append("\n"))
      sb.append("\n")
    }

    sb.toString
  }

  def toTrelloCard: Card = {
    val card = new Card()

    card.setDateLastActivity(createdAt)
    card.setName(getTitle)

    val desc = description
    card.setDesc(desc)
    card
  }
}

object UserTweetProtocol extends DefaultJsonProtocol {

  import ExternalURlProtocol._
  import UserProtocol._

  implicit val userTweetJsonFormat: RootJsonFormat[UserTweet] = jsonFormat5(UserTweet)
}
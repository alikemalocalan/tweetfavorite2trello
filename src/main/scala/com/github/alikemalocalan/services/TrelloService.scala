package com.github.alikemalocalan.services

import com.github.alikemalocalan.Config
import com.github.alikemalocalan.model.UserTweet
import com.julienvey.trello.Trello
import com.julienvey.trello.domain.{Board, Card, TList}
import com.julienvey.trello.impl.TrelloImpl
import com.julienvey.trello.impl.http.ApacheHttpClient

import scala.jdk.CollectionConverters._

object TrelloService extends Config {

  val trelloApi = new TrelloImpl(trelloAccessToken, trelloAccessSecret, new ApacheHttpClient())

  def importToTrello(boardId: String, boardIdListId: String, tweets: List[UserTweet]): Unit = {
    val cardList = getUserCards(boardId)
    tweets.foreach { tweet =>
      val entityCard =
        cardList.find(card => card.getName.startsWith(tweet.tweetID.toString))
          .map(card => updateCardFromTweet(card.getId, tweet))
          .getOrElse(createCardFromTweet(boardIdListId, tweet))
      updateCardAttachment(tweet, entityCard, trelloApi)
    }

  }

  def updateCardAttachment(tweet: UserTweet, card: Card, trelloApi: Trello): Unit = {
    val attachmentList = trelloApi.getCardAttachments(card.getId).asScala
    tweet.urls.getImageNames.foreach { case (url, name) =>
      if (!attachmentList.exists(_.getName == name))
        trelloApi.addUrlAttachmentToCard(card.getId, url)
    }
    tweet.urls.expanded_url.foreach(url =>
      if (!attachmentList.map(_.getUrl).contains(url))
        trelloApi.addUrlAttachmentToCard(card.getId, url)
    )
    tweet.urls.videoUrl.foreach(url =>
      if (!attachmentList.map(_.getUrl).contains(url))
        trelloApi.addUrlAttachmentToCard(card.getId, url)
    )
  }

  def getUserBoards(trelloUserName: String): Option[Board] =
    trelloApi.getMemberBoards(trelloUserName)
      .asScala
      .headOption

  def getUserBoardIds(trelloUserName: String): Option[String] =
    getUserBoards(trelloUserName).map(_.getId)

  def getUserBoardLists(boardId: String): Seq[TList] =
    trelloApi.getBoardLists(boardId)
      .asScala.toSeq

  def getUserCards(boardId: String): Seq[Card] =
    trelloApi.getBoardCards(boardId)
      .asScala.toSeq

  def createCardFromTweet(boardListId: String, tweet: UserTweet): Card =
    trelloApi.createCard(boardListId, tweet.toTrelloCard()).update()

  def updateCardFromTweet(cardId: String, tweet: UserTweet): Card =
    trelloApi.updateCard(tweet.toTrelloCard(Some(cardId))).update()

}

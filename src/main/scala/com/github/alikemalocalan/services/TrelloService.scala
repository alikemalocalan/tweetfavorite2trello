package com.github.alikemalocalan.services

import com.github.alikemalocalan.Config
import com.github.alikemalocalan.model.UserTweet
import com.julienvey.trello.Trello
import com.julienvey.trello.domain.Card
import com.julienvey.trello.impl.TrelloImpl
import com.julienvey.trello.impl.http.ApacheHttpClient

import scala.jdk.CollectionConverters._

object TrelloService extends Config {

  val trelloApi = new TrelloImpl(trelloAccessToken, trelloAccessSecret, new ApacheHttpClient())

  def importToTrello(trelloUserId: String, tweets: Array[UserTweet]): Unit = {
    val userBoardId = trelloApi.getMemberBoards(trelloUserId).get(0).getId
    val listId = trelloApi.getBoardLists(userBoardId).get(0).getId
    val cardList = trelloApi.getBoardCards(userBoardId).asScala

    tweets.foreach { tweet =>
      val entityCard =
        cardList.find(card => card.getName.startsWith(tweet.tweetID.toString)) // TODO: change with if-else
          .map { card =>
            val updatedCard = tweet.toTrelloCard
            updatedCard.setId(card.getId)
            updatedCard
          }.getOrElse(trelloApi.createCard(listId, tweet.toTrelloCard).update())

      entityCard.setName(tweet.getTitle)

      val finalCard = trelloApi.updateCard(entityCard).update()
      updateCardAttachment(tweet, finalCard, trelloApi)
    }

  }

  def updateCardAttachment(tweet: UserTweet, card: Card, trelloApi: Trello): Unit = {
    tweet.urls.imageUrls.foreach(url =>
      trelloApi.addUrlAttachmentToCard(card.getId, url)
    )
    tweet.urls.expanded_url.foreach(url =>
      trelloApi.addUrlAttachmentToCard(card.getId, url)
    )
    tweet.urls.videoUrl.foreach(url =>
      trelloApi.addUrlAttachmentToCard(card.getId, url)
    )
  }

}

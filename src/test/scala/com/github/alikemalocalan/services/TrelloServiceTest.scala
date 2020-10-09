package com.github.alikemalocalan.services

import org.scalatest.flatspec._
import org.scalatest.matchers.should.Matchers


class TrelloServiceTest extends AnyFlatSpec with Matchers {

  ignore should "importToTrello" in {

    val trelloTestUserId = "alikemalocalan"

    val tweets = TwitterServices.getFavoritesWithPagination(page = 1, maxTweetNumber = 15)

    val boardId = TrelloService.getUserBoardIds(trelloTestUserId).head

    val boardListId = TrelloService.getUserBoardLists(boardId).head.getId

    TrelloService.importToTrello(boardId, boardListId, tweets)
  }


}

package com.github.alikemalocalan.model

import spray.json.{DefaultJsonProtocol, RootJsonFormat}

case class TrelloBoard(id: String, name: String)

object TrelloBoardProtocol extends DefaultJsonProtocol {
  implicit val externalUrlJsonFormat: RootJsonFormat[TrelloBoard] = jsonFormat2(TrelloBoard)
}

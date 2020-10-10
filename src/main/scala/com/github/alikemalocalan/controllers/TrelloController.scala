package com.github.alikemalocalan.controllers

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.github.alikemalocalan.model.TrelloBoardProtocol._
import com.github.alikemalocalan.services.TrelloService._

object TrelloController {

  val boardRoute: Route = path("trello" / "boards") {
    pathEndOrSingleSlash {
      get {
        parameters(Symbol("username").as[String], Symbol("token").as[Int], Symbol("tokensecret").as[Int]) { (username, token, tokensecret) =>
          complete(getUserBoards(username))
        }
      }
    }
  }
}

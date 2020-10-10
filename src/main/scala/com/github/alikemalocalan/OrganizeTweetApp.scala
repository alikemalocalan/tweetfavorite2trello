package com.github.alikemalocalan

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import com.github.alikemalocalan.controllers.TrelloController.boardRoute
import com.github.alikemalocalan.controllers.TweetController.tweetsRoute

import scala.concurrent.ExecutionContextExecutor
import scala.util.{Failure, Success}

object OrganizeTweetApp extends Config {
  def main(args: Array[String]): Unit = {

    implicit val system: ActorSystem = ActorSystem()
    val logger = system.log
    implicit val executionContext: ExecutionContextExecutor = system.dispatcher

    val routes = boardRoute ~ tweetsRoute

    Http().newServerAt(address, port).bindFlow(routes)
      .onComplete {
        case Success(b) => logger.info(s"application is up and running at ${b.localAddress.getHostName}:${b.localAddress.getPort}")
        case Failure(e) => logger.error(s"could not start application: {}", e.getMessage)
      }
  }

}

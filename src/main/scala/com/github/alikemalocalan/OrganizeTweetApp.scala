package com.github.alikemalocalan

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import com.github.alikemalocalan.controllers.TweetController.getTweetsRoute

import scala.concurrent.ExecutionContextExecutor
import scala.util.{Failure, Success}

object OrganizeTweetApp extends Config {
  def main(args: Array[String]): Unit = {

    implicit val system: ActorSystem = ActorSystem()
    val logger = system.log
    implicit val executionContext: ExecutionContextExecutor = system.dispatcher

    Http().newServerAt(address, port).bindFlow(getTweetsRoute)
      .onComplete {
        case Success(b) => logger.info(s"application is up and running at ${b.localAddress.getHostName}:${b.localAddress.getPort}")
        case Failure(e) => logger.error(s"could not start application: {}", e.getMessage)
      }
  }

}

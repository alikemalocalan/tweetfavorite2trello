package com.github.alikemalocalan

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import com.github.alikemalocalan.model.FavoriteResult
import com.github.alikemalocalan.services.TwitterServices

import scala.util.{Failure, Success}

object OrganizeTweetApp extends Config {
  def main(args: Array[String]): Unit = {
    implicit val system = ActorSystem()
    val logger = system.log
    implicit val materializer = ActorMaterializer()

    // needed for the future flatMap/onComplete in the end
    implicit val executionContext = system.dispatcher

    val route = path("tweets") {
      pathEndOrSingleSlash {
        get {
          parameters(Symbol("page").as[Int].?, Symbol("count").as[Int].?) { (page, count) =>
            import com.github.alikemalocalan.model.FavoriteResultProtocol._

            val pageNumber = page.getOrElse(1)
            val countNumber = count.getOrElse(50)
            val result: FavoriteResult = TwitterServices.getFavoritesWithPagination(page = pageNumber, maxTweetNumber = countNumber)
            complete(result)
          }
        }
      }
    }

    Http().bindAndHandle(route, address, port)
      .onComplete {
        case Success(b) => logger.info(s"application is up and running at ${b.localAddress.getHostName}:${b.localAddress.getPort}")
        case Failure(e) => logger.error(s"could not start application: {}", e.getMessage)
      }
  }

}

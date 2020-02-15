package com.github.alikemalocalan.controller

import com.github.alikemalocalan.services.TwitterServices
import org.json4s._
import org.json4s.jackson.Serialization._
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{GetMapping, RequestParam, ResponseBody}


@Controller("/tweets")
class TweetController {
  implicit val formats: DefaultFormats.type = DefaultFormats

  @GetMapping(produces = Array(MediaType.APPLICATION_JSON_VALUE))
  @ResponseBody
  def handleRequest(@RequestParam(value = "page", defaultValue = "1", required = true) page: String,
                    @RequestParam(value = "count", defaultValue = "50", required = true) count: String): String =
    write(TwitterServices.getFavorites(page = page.toInt, maxTweetNumber = count.toInt))

}

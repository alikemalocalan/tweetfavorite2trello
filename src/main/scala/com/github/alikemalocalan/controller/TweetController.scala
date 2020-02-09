package com.github.alikemalocalan.controller

import java.util.Optional

import com.github.alikemalocalan.services.TwitterServices
import org.json4s._
import org.json4s.jackson.Serialization._
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{GetMapping, RequestParam, ResponseBody}


@Controller
class TweetController {
  implicit val formats: DefaultFormats.type = DefaultFormats

  @GetMapping(path = Array("/tweets"),
    produces = Array(MediaType.APPLICATION_JSON_VALUE))
  @ResponseBody
  def handleRequest(@RequestParam(defaultValue = "1", required = false) page: Optional[String],
                    @RequestParam(defaultValue = "50", required = false) count: Optional[String]): String =
    write(TwitterServices.getFavorites(page = page.orElse("1").toInt, maxTweetNumber = count.orElse("50").toInt))

}

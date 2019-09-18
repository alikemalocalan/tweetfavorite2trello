package com.github.alikemalocalan.controller

import com.github.alikemalocalan.services.TwitterServices
import org.json4s._
import org.json4s.jackson.Serialization._
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestMapping, RequestMethod, ResponseBody}

@Controller
class TweetController {
  implicit val formats: DefaultFormats.type = DefaultFormats

  @RequestMapping(path = Array("/tweets"), method = Array(RequestMethod.GET), produces = Array(MediaType.APPLICATION_JSON_VALUE))
  @ResponseBody
  def handleRequest(): String = write(TwitterServices.getFavorites())

}

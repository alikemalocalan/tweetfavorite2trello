package com.github.alikemalocalan.util

import java.text.{ParseException, SimpleDateFormat}
import java.util.Date

import spray.json.{JsString, JsValue, JsonFormat, deserializationError}

object Utils {

  implicit object DateFormat extends JsonFormat[Date] {
    private val localIsoDateFormatter = new ThreadLocal[SimpleDateFormat] {
      override def initialValue() = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    }

    def parseIsoDateString(date: String): Option[Date] = {
      if (date.length != 28) None
      else try Some(localIsoDateFormatter.get().parse(date))
      catch {
        case p: ParseException => None
      }
    }

    def dateToIsoString(date: Date) = localIsoDateFormatter.get().format(date)

    def write(date: Date): JsValue = JsString(dateToIsoString(date))

    def read(json: JsValue): Date = json match {

      case JsString(rawDate) => parseIsoDateString(rawDate) match {
        case None => deserializationError(s"Expected ISO Date format, got $rawDate")
        case Some(isoDate) => isoDate
      }

      case unknown => deserializationError(s"Expected JsString, got $unknown")
    }
  }

}



package org.marighella.image_storage.api

import akka.event.Logging
import akka.http.scaladsl.server.{ Directives, Route }
import org.marighella.image_storage.ServerComponents

trait Routes extends Directives {
  self: ServerComponents =>

  lazy val routes: Route = {
    logRequestResult("Image Storage Service API", Logging.DebugLevel) {
      uploadRoute.route
    }
  }
}

package org.marighella.image_storage

import akka.http.scaladsl.Http
import org.marighella.image_storage.api.Routes
import org.marighella.image_storage.config.{ AwsS3Config, ServerConfig }
import org.marighella.image_storage.logging.LoggingSupport
import org.marighella.image_storage.utils.ExecutionSupport

import scala.concurrent.Future
import scala.util.{ Failure, Success }

object Server
    extends App
    with ServerConfig
    with AwsS3Config
    with ExecutionSupport
    with Routes
    with ServerComponents
    with LoggingSupport {

  private val bindHttp: Future[Http.ServerBinding] =
    Http(system).bindAndHandle(handler = routes, interface = apiConfig.server.uri, port = apiConfig.server.port)

  bindHttp.onComplete {
    case Success(_) => logger.info(s"Started service at ${apiConfig.server.uri}:${apiConfig.server.port}")
    case Failure(ex) => logger.info(s"Error starting service! Error: ${ex.getMessage}")
  }

  sys.addShutdownHook {
    bindHttp.flatMap(_.unbind()).onComplete { _ =>
      materializer.shutdown()
      system.terminate()
    }
  }
}

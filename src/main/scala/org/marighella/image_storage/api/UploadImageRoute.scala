package org.marighella.image_storage.api

import java.nio.file.Paths

import akka.actor.ActorSystem
import akka.http.scaladsl.server.Route
import akka.pattern.CircuitBreaker
import akka.stream.{ ActorMaterializer, IOResult }
import akka.stream.scaladsl.FileIO
import org.marighella.image_storage.config.ApiConfig

import scala.concurrent.Future

class UploadImageRoute(apiConfig: ApiConfig)(implicit actorSystem: ActorSystem, materializer: ActorMaterializer)
    extends ApiSupport {

  override protected val config: ApiConfig = apiConfig

  override protected val routeCircuitBreaker: CircuitBreaker =
    circuitBreaker(actorSystem.dispatcher, actorSystem.scheduler)

  val route: Route =
    ignoreTrailingSlash {
      pathPrefix("upload") {
        post {
          fileUpload("test") {
            case (metadata, byteSource) =>
              val file = Paths.get("file.png")
              val runner: Future[IOResult] = byteSource.runWith(FileIO.toPath(file))
              onSuccess(runner) { r =>
                if (r.wasSuccessful) complete("OK")
                else complete("Error")
              }
          }
        }
      }
    }
}

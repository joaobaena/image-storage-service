package org.marighella.image_storage.api

import akka.actor.ActorSystem
import akka.http.scaladsl.model.{ ContentTypes, HttpEntity, HttpResponse, StatusCodes }
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.FileInfo
import akka.pattern.CircuitBreaker
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Source
import akka.util.ByteString
import monix.execution.Scheduler
import org.marighella.image_storage.config.ApiConfig
import org.marighella.image_storage.service.FileDownloadService

class UploadImageRoute(apiConfig: ApiConfig, fileDownloadService: FileDownloadService)(
  implicit s: Scheduler,
  actorSystem: ActorSystem,
  materializer: ActorMaterializer
) extends ApiSupport {

  override protected val config: ApiConfig = apiConfig

  override protected val routeCircuitBreaker: CircuitBreaker =
    circuitBreaker(actorSystem.dispatcher, actorSystem.scheduler)

  lazy val route: Route =
    uploadFileWithMetadata { (metadata, uploadStream) =>
      handleServiceCall(fileDownloadService.streamDownload(metadata, uploadStream)) { result =>
        logger.info(s"Successfully stored file ${metadata.fileName}")
        // TODO: Fix sending an 2xx 'early' response before end of request was received
        HttpResponse(StatusCodes.OK, entity = HttpEntity(ContentTypes.`application/json`, result.toString))
      }
    }

  private def uploadFileWithMetadata(block: (FileInfo, Source[ByteString, Any]) => Route): Route =
    ignoreTrailingSlash {
      pathPrefix("image-storage-service" / "api" / "upload-photo") {
        path(Segment) { collection =>
          post {
            handleRequest {
              fileUpload(collection) {
                case (metadata, uploadStream) =>
                  // TODO: Validate metadata
                  block(metadata, uploadStream)
              }
            }
          }
        }
      }
    }
}

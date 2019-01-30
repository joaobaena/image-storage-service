package org.marighella.image_storage.api

import java.util.concurrent.TimeoutException

import akka.actor.{ Scheduler => AkkaScheduler }
import akka.http.scaladsl.coding.{ Gzip, NoCoding }
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.{ Directives, RejectionHandler, Route }
import akka.pattern.CircuitBreaker
import io.circe.Encoder
import io.circe.syntax._
import monix.execution.Scheduler
import org.marighella.image_storage.config.ApiConfig
import org.marighella.image_storage.logging.LoggingSupport
import org.marighella.image_storage.messages.ErrorResponse
import org.marighella.image_storage.service.AsyncResult
import org.marighella.image_storage.utils.CirceJsonSupport

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._
import scala.util.control.NonFatal
import scala.util.{ Failure, Success }

trait ApiSupport extends Directives with LoggingSupport with CirceJsonSupport {
  protected val routeCircuitBreaker: CircuitBreaker
  protected val config: ApiConfig

  protected def circuitBreaker(ec: ExecutionContext, scheduler: AkkaScheduler) =
    new CircuitBreaker(
      scheduler,
      config.circuitBreaker.maxFailures,
      config.requestTimeout + 10.seconds,
      config.circuitBreaker.resetTimeout,
      config.circuitBreaker.maxResetTimeout,
      config.circuitBreaker.backoffFactor
    )(ec)

  protected def asJsonEntity[A: Encoder](data: A): ResponseEntity =
    HttpEntity(ContentTypes.`application/json`, data.asJson.noSpaces)

  protected def handleRequest(route: Route): Route =
    encodeResponseWith(NoCoding, Gzip) {
      decodeRequestWith(NoCoding, Gzip) {
        extractRequest { request =>
          handleRejections(rejectionHandler(request.getUri().getPathString))(route)
        }
      }
    }

  protected def handleServiceCall[R](
    call: => AsyncResult[R]
  )(onSuccess: R => HttpResponse)(implicit scheduler: Scheduler): Route = {
    val value = call.timeout(config.requestTimeout).runToFuture(scheduler)
    onCompleteWithBreaker(routeCircuitBreaker)(value) {
      case Success(response) =>
        complete {
          response match {
            case Left(failed) =>
              // TODO: Circuit breaker should act depending on error type
              val msg = failed.message
              val error = ErrorResponse(msg, "undefined")
              logger.error(s"Internal error: $msg")
              HttpResponse(StatusCodes.InternalServerError, entity = asJsonEntity(error))
            case Right(data) => onSuccess(data)
          }
        }
      case Failure(exception) =>
        exception match {
          case ex: TimeoutException =>
            logger.error("Request timeout")
            val error = ErrorResponse("Execution timed out", "undefined")
            complete(HttpResponse(StatusCodes.ServiceUnavailable, entity = asJsonEntity(error)))
          case NonFatal(ex) =>
            logger.error("Unexpected error handling the request")
            val error = ErrorResponse(s"Unexpected error: ${ex.getMessage}", "undefined")
            complete(HttpResponse(StatusCodes.InternalServerError, entity = asJsonEntity(error)))
        }
    }
  }

  protected def rejectionHandler(path: String): RejectionHandler = {
    val defaultJsonResponse = RejectionHandler.default.mapRejectionResponse {
      case response @ HttpResponse(_, _, entity: HttpEntity.Strict, _) =>
        val message = entity.data.utf8String.replaceAll("\"", """\"""").replaceAll("\n", "")
        logger.error("HTTP Request failed")
        val error = ErrorResponse(message, path)
        response.copy(entity = asJsonEntity(error))
      case rsp => rsp
    }
    RejectionHandler
      .newBuilder()
      .handleCircuitBreakerOpenRejection { rejection =>
        logger.error(
          s"Path $path is failing, circuit breaker still open for ${rejection.cause.remainingDuration.toMillis}ms"
        )
        val error = ErrorResponse("Service is unavailable due to previous failures", path)
        complete(HttpResponse(StatusCodes.ServiceUnavailable, entity = asJsonEntity(error)))
      }
      .result()
      .withFallback(defaultJsonResponse)
  }

}

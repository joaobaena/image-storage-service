package org.marighella.image_storage.utils

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import monix.execution.{ Scheduler, UncaughtExceptionReporter }
import org.slf4j.LoggerFactory

trait ExecutionSupport {
  implicit val system: ActorSystem = ActorSystem("image-store-service")
  implicit val materializer: ActorMaterializer = ActorMaterializer()(system)

  private val slf4jMonix = UncaughtExceptionReporter { ex =>
    LoggerFactory.getLogger("monix").error("Uncaught exception", ex)
  }
  implicit val defaultScheduler: Scheduler = Scheduler(system.dispatcher, slf4jMonix)
}

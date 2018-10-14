package org.marighella.image_storage.config

import pureconfig.loadConfigOrThrow

import scala.concurrent.duration.FiniteDuration

trait ServerConfig {
  lazy val apiConfig: ApiConfig = loadConfigOrThrow[ApiConfig]("image-storage-service.api-config")
}

final case class HttpConfig(uri: String, port: Int)

final case class ApiConfig(server: HttpConfig, requestTimeout: FiniteDuration, circuitBreaker: CircuitBreakerConfig)

final case class CircuitBreakerConfig(
  maxFailures: Int,
  resetTimeout: FiniteDuration,
  maxResetTimeout: FiniteDuration,
  backoffFactor: Double
)

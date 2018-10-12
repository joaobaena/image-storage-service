package org.marighella.image_storage.logging

import net.logstash.logback.marker.Markers.appendEntries
import org.slf4j.{ Logger, MDC }

import scala.collection.JavaConverters._

class LogBuilder(logger: Logger) {

  def kv(key: String, value: String): LogBuilder = keyValue(key, value)

  def kv(key: String, value: AnyRef): LogBuilder = keyValue(key, value)

  def isEmpty(value: String): Boolean = value == null && value.trim.isEmpty

  val keyValues: scala.collection.mutable.Map[String, Object] = scala.collection.mutable.Map()

  def keyValue(key: String, value: String): LogBuilder = {
    if (!isEmpty(key) && !isEmpty(value)) {
      keyValues.put(key, value)
    }
    this
  }
  def keyValue(key: String, value: AnyRef): LogBuilder = {
    if (!isEmpty(key) && value != null && !isEmpty(value.toString)) {
      keyValues.put(key, value)
    }
    this
  }
  def contextKeyValue(key: String, value: AnyRef): Unit =
    if (!isEmpty(key) && !isEmpty(value.toString)) {
      MDC.put(key, value.toString)
    }

  def error(message: String): Unit = logger.error(message, appendEntries(keyValues.asJava))

  def error(message: String, t: Throwable): Unit = logger.error(appendEntries(keyValues.asJava), message, t)

  def warn(message: String): Unit = logger.warn(message, appendEntries(keyValues.asJava))

  def warn(message: String, t: Throwable): Unit = logger.warn(appendEntries(keyValues.asJava), message, t)

  def info(message: String): Unit = logger.info(message, appendEntries(keyValues.asJava))

  def trace(message: String): Unit = logger.trace(message, appendEntries(keyValues.asJava))

}

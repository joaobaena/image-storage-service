package org.marighella.image_storage.logging

import org.slf4j.LoggerFactory

trait LoggingSupport {
  def logger: LogBuilder = new LogBuilder(LoggerFactory.getLogger(getClass.getName))
}

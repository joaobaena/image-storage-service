package org.marighella.image_storage.storage

import akka.Done
import akka.http.scaladsl.server.directives.FileInfo
import akka.stream.scaladsl.Sink
import akka.util.ByteString
import org.marighella.image_storage.service.AsyncResult

class AwsS3FileStorage extends FileStorage {
  def storeFile(metadata: FileInfo): Sink[ByteString, AsyncResult[Done]] = ???

}

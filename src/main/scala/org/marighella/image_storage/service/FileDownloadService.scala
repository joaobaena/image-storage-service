package org.marighella.image_storage.service

import java.nio.file.Paths

import akka.http.scaladsl.server.directives.FileInfo
import akka.stream.Materializer
import akka.stream.scaladsl.{ FileIO, Source }
import akka.util.ByteString

trait FileDownloadService {
  def storeFile(metadata: FileInfo, uploadStream: Source[ByteString, Any])(
    implicit mat: Materializer
  ): AsyncResult[Boolean]
}

class ImageDownloadService extends FileDownloadService {
  override def storeFile(metadata: FileInfo, uploadStream: Source[ByteString, Any])(
    implicit mat: Materializer
  ): AsyncResult[Boolean] = {
    val file = Paths.get(metadata.fileName)
    uploadStream
      .runWith(FileIO.toPath(file))
      .asServiceCall
      .map(_.map(c => c.wasSuccessful))
  }
}

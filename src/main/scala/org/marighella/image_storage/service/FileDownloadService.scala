package org.marighella.image_storage.service

import akka.Done
import akka.http.scaladsl.server.directives.FileInfo
import akka.stream.Materializer
import akka.stream.scaladsl.{ Source }
import akka.util.ByteString
import org.marighella.image_storage.storage.FileStorage

trait FileDownloadService {
  def streamDownload(metadata: FileInfo, uploadStream: Source[ByteString, Any])(
    implicit mat: Materializer
  ): AsyncResult[Done]
}

class ImageDownloadService(storage: FileStorage) extends FileDownloadService {

  override def streamDownload(metadata: FileInfo, uploadStream: Source[ByteString, Any])(
    implicit mat: Materializer
  ): AsyncResult[Done] = uploadStream.runWith(storage.storeFile(metadata))

}

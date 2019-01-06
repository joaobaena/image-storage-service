package org.marighella.image_storage.service

import akka.Done
import akka.http.scaladsl.server.directives.FileInfo
import akka.stream.Materializer
import akka.stream.scaladsl.Source
import akka.util.ByteString
import org.marighella.image_storage.storage.FileStorage

trait FileDownloadService {
  type UploadStreamFn = (FileInfo, Source[ByteString, Any]) => AsyncResult[Done]

  val streamDownload: UploadStreamFn
}

class ImageDownloadService(storage: FileStorage)(implicit mat: Materializer) extends FileDownloadService {

  override val streamDownload = (metadata: FileInfo, uploadStream: Source[ByteString, Any]) =>
    uploadStream.runWith(storage.storeFile(metadata))

}

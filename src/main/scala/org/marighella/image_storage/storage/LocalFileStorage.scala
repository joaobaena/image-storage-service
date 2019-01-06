package org.marighella.image_storage.storage

import java.nio.file.Paths

import akka.Done
import akka.http.scaladsl.server.directives.FileInfo
import akka.stream.scaladsl.FileIO
import org.marighella.image_storage.service._

class LocalFileStorage extends FileStorage {
  override val storeFile: StoreResultFn = (metadata: FileInfo) => {
    val file = Paths.get(metadata.fileName)
    FileIO
      .toPath(file)
      .mapMaterializedValue(
        _.asServiceCall
          .tmap(_ => Done)
      )
  }
}

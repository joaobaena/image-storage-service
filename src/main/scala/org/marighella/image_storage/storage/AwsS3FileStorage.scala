package org.marighella.image_storage.storage

import akka.Done
import akka.http.scaladsl.server.directives.FileInfo
import org.marighella.image_storage.config.AwsS3Config
import org.marighella.image_storage.service._

class AwsS3FileStorage extends FileStorage with AwsS3Support with AwsS3Config {
  override val storeFile: StoreResultFn = (metadata: FileInfo) =>
    s3StoreFile(metadata.fileName)
      .mapMaterializedValue(
        _.asServiceCall
          .tmap(_ => Done)
    )
}

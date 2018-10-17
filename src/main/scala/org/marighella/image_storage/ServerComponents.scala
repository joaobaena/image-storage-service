package org.marighella.image_storage

import org.marighella.image_storage.api.UploadImageRoute
import org.marighella.image_storage.config.ServerConfig
import org.marighella.image_storage.service.ImageDownloadService
import org.marighella.image_storage.storage.AwsS3FileStorage
import org.marighella.image_storage.utils.ExecutionSupport

trait ServerComponents {
  self: ExecutionSupport with ServerConfig =>

  val fileStorage = new AwsS3FileStorage

  val storeService = new ImageDownloadService(fileStorage)

  val uploadRoute = new UploadImageRoute(apiConfig, storeService)

}

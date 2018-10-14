package org.marighella.image_storage

import org.marighella.image_storage.api.UploadImageRoute
import org.marighella.image_storage.config.ServerConfig
import org.marighella.image_storage.utils.ExecutionSupport

trait ServerComponents {
  self: ExecutionSupport with ServerConfig =>

  val uploadRoute = new UploadImageRoute(apiConfig)

}

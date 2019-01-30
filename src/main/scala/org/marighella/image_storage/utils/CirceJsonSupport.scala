package org.marighella.image_storage.utils

import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport
import io.circe.generic.extras.{ AutoDerivation, Configuration }

trait CirceJsonSupport extends FailFastCirceSupport with AutoDerivation {
  protected implicit val snakeCaseConfig: Configuration = Configuration.default.withSnakeCaseMemberNames.withDefaults
}

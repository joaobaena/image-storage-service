package org.marighella.image_storage.config

import pureconfig.loadConfigOrThrow

trait AwsS3Config {
  lazy val awsS3Config: AwsS3Settings = loadConfigOrThrow[AwsS3Settings]("image-storage-service.aws-s3")
}

final case class AwsS3Settings(
  accessKey: String,
  secretAccessKey: String,
  storePath: String,
  bucketName: String,
  region: String
)

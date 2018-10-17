package org.marighella.image_storage.storage

import akka.stream.alpakka.s3.impl.{ S3Headers, ServerSideEncryption }
import akka.stream.alpakka.s3.scaladsl.{ MultipartUploadResult, S3Client }
import akka.stream.scaladsl.Sink
import akka.util.ByteString
import com.amazonaws.auth.{ AWSStaticCredentialsProvider, BasicAWSCredentials }
import org.marighella.image_storage.config.AwsS3Config
import org.marighella.image_storage.utils.ExecutionSupport

import scala.concurrent.Future

trait AwsS3Support extends ExecutionSupport {
  self: AwsS3Config =>

  protected val credentialsProvider =
    new AWSStaticCredentialsProvider(new BasicAWSCredentials(awsS3Config.accessKey, awsS3Config.secretAccessKey))

  protected val s3client: S3Client =
    S3Client(credentialsProvider, awsS3Config.region)

  protected val bucketName: String = awsS3Config.bucketName

  protected def s3StoreFile(fileName: String): Sink[ByteString, Future[MultipartUploadResult]] =
    s3client
      .multipartUploadWithHeaders(
        awsS3Config.bucketName,
        fileName,
        s3Headers = Some(S3Headers(ServerSideEncryption.AES256))
      )
}

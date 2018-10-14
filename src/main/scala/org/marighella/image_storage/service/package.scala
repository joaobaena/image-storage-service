package org.marighella.image_storage

import monix.eval.Task
import monix.execution.misc.NonFatal
import org.marighella.image_storage.messages.{ ServerStorageError, ServiceError }

import scala.concurrent.Future

package object service {
  type AsyncResult[R] = Task[Either[ServiceError, R]]

  implicit class FutureTask[T](asynchronous: => Future[T]) {
    def asServiceCall: AsyncResult[T] =
      Task
        .deferFutureAction(implicit scheduler => asynchronous)
        .map(res => Right(res))
        .onErrorRecover {
          case NonFatal(ex) => Left(ServerStorageError(ex.getMessage))
        }
  }

}

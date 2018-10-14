package org.marighella.image_storage.messages

sealed trait ServiceError extends Product with Serializable {
  def message: String
}

final case class InternalError(message: String) extends ServiceError
final case class DatabaseError(message: String) extends ServiceError
final case class ServerStorageError(message: String) extends ServiceError

package org.marighella.image_storage.messages

sealed trait Error extends Product with Serializable {
  def message: String
}

final case class DatabaseError(message: String) extends Error
final case class ServerStorageError(message: String) extends Error

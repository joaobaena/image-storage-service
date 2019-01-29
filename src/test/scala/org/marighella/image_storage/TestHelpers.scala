package org.marighella.image_storage
import org.scalacheck.Shrink
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.{ Seconds, Span }
import org.scalatest.{ EitherValues, Matchers, TryValues, WordSpec }

trait TestHelpers extends WordSpec with Matchers with ScalaFutures with TryValues with EitherValues {

  implicit def noShrink[T]: Shrink[T] = Shrink.shrinkAny

  implicit val defaultPatience: PatienceConfig = PatienceConfig(timeout = Span(5, Seconds))
}

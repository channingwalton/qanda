package io.questions.util.collection

import cats.data.NonEmptyList
import io.circe._
import io.circe.syntax._

object JsonCodecs {
  implicit def nonEmptyListEncoder[T: Encoder]: Encoder[NonEmptyList[T]] =
    (a: NonEmptyList[T]) => a.toList.asJson

  implicit def nonEmptyListDecoder[T: Decoder](implicit ld: Decoder[List[T]]): Decoder[NonEmptyList[T]] =
    (c: HCursor) => ld(c).map(NonEmptyList.fromListUnsafe)
}

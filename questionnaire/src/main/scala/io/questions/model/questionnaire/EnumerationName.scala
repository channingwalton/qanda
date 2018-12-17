package io.questions.model.questionnaire

import cats.{ Eq, Show }
import io.circe._

case class EnumerationName(value: String)

object EnumerationName {
  implicit val keyEncoder: KeyEncoder[EnumerationName] = (a: EnumerationName) => a.value
  implicit val keyDecoder: KeyDecoder[EnumerationName] = (s: String) => Some(EnumerationName(s))

  implicit val encoder: Encoder[EnumerationName] = (a: EnumerationName) => Json.fromString(a.value)
  implicit val decoder: Decoder[EnumerationName] = (c: HCursor) => c.as[String].map(EnumerationName(_))

  implicit val equals: Eq[EnumerationName] = Eq.fromUniversalEquals

  implicit val show: Show[EnumerationName] = (t: EnumerationName) => t.value
}

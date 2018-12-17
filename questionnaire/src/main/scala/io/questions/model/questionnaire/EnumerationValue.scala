package io.questions.model.questionnaire

import cats.{ Eq, Show }
import io.circe._
import io.questions.model.EncoderHelpers

case class EnumerationValue(key: String, value: String)

object EnumerationValue extends EncoderHelpers {
  implicit val encoder: Encoder[EnumerationValue] = deriveCustomEncoder
  implicit val decoder: Decoder[EnumerationValue] = deriveCustomDecoder

  implicit val equals: Eq[EnumerationValue] = Eq.fromUniversalEquals

  implicit val show: Show[EnumerationValue] = (t: EnumerationValue) => t.value

  // we make sure we have valid keys for html representation (no spaces, no odd characters)
  def of(value: String): EnumerationValue = {
    val asKey = value.trim.filter(_.isLetterOrDigit)
    EnumerationValue(asKey, value)
  }
}

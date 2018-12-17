package io.questions.testdata.samples.enumerations

import cats.kernel.Eq
import io.circe.{ Decoder, Encoder }
import io.questions.model.EncoderHelpers

final case class Currency(isoCode: String, name: String)

object Currency extends EncoderHelpers {
  implicit val encoder: Encoder[Currency] = deriveCustomEncoder
  implicit val decoder: Decoder[Currency] = deriveCustomDecoder

  implicit val equals: Eq[Currency] = Eq.fromUniversalEquals[Currency]
}

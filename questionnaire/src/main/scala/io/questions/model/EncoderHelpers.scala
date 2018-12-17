package io.questions.model

import io.circe.generic.extras._
import io.circe.generic.extras.decoding.ConfiguredDecoder
import io.circe.generic.extras.encoding.ConfiguredObjectEncoder
import io.circe.generic.extras.semiauto._
import io.circe.{ Decoder, Encoder, JsonObject }
import shapeless.Lazy

trait EncoderHelpers {
  // NOTE: this requires semiauto from 'extras'package to be used in decoders/encoders
  implicit val config: Configuration = Configuration.default.withDefaults.withDiscriminator("type")

  def deriveCustomEncoder[A](implicit encode: Lazy[ConfiguredObjectEncoder[A]]): Encoder[A] =
    deriveEncoder[A].mapJsonObject(excludeNullsFromJson)

  def deriveCustomDecoder[A](implicit decode: Lazy[ConfiguredDecoder[A]]): Decoder[A] =
    deriveDecoder[A]

  private def excludeNullsFromJson(jsonObject: JsonObject): JsonObject =
    jsonObject.filter {
      case (_, value) => !value.isNull
    }
}

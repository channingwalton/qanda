/*
 * Copyright 2018 TBD
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.questions.model

import io.circe.generic.extras._
import io.circe.generic.extras.decoding.ConfiguredDecoder
import io.circe.generic.extras.encoding.ConfiguredObjectEncoder
import io.circe.generic.extras.semiauto._
import io.circe.{ Decoder, Encoder, JsonObject }
import shapeless.Lazy

trait EncoderHelpers {
  // NOTE: this requires semiauto from 'extras' package to be used in decoders/encoders
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

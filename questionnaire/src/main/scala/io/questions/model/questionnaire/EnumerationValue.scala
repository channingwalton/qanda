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

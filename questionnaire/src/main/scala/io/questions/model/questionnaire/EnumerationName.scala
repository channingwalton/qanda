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

case class EnumerationName(value: String)

object EnumerationName {
  implicit val keyEncoder: KeyEncoder[EnumerationName] = (a: EnumerationName) => a.value
  implicit val keyDecoder: KeyDecoder[EnumerationName] = (s: String) => Some(EnumerationName(s))

  implicit val encoder: Encoder[EnumerationName] = (a: EnumerationName) => Json.fromString(a.value)
  implicit val decoder: Decoder[EnumerationName] = (c: HCursor) => c.as[String].map(EnumerationName(_))

  implicit val equals: Eq[EnumerationName] = Eq.fromUniversalEquals

  implicit val show: Show[EnumerationName] = (t: EnumerationName) => t.value
}

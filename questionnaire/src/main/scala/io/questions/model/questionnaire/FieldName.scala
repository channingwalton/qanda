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

final case class FieldName(value: String) extends AnyVal

object FieldName {
  implicit val encoder: Encoder[FieldName] = (a: FieldName) => Json.fromString(a.value)
  implicit val decoder: Decoder[FieldName] = _.as[String].map(FieldName(_))

  implicit val equals: Eq[FieldName] = Eq.fromUniversalEquals[FieldName]

  implicit val show: Show[FieldName] = (t: FieldName) => t.value

  implicit class FieldNameStringSyntax(s: String) {
    def fieldName: FieldName = FieldName(s)
  }
}

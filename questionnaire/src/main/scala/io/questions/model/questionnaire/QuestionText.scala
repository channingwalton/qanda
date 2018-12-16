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
import io.circe.{ Decoder, Encoder, HCursor, Json }

case class QuestionText(value: String) extends AnyVal

object QuestionText {
  implicit val equals: Eq[QuestionText] = Eq.fromUniversalEquals

  implicit val show: Show[QuestionText] = (t: QuestionText) => t.value

  implicit class QuestionTextSyntax(value: String) {
    def text: QuestionText = QuestionText(value)
  }

  implicit val encoder: Encoder[QuestionText] = (a: QuestionText) => Json.fromString(a.value)
  implicit val decoder: Decoder[QuestionText] = (c: HCursor) => c.as[String].map(QuestionText(_))
}

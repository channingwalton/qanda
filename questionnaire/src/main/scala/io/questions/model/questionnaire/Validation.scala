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

import io.circe.{ Decoder, Encoder }
import io.questions.model.EncoderHelpers

sealed trait Validation extends Product with Serializable

object Validation extends EncoderHelpers {
  case object AlwaysValid                                                   extends Validation
  case class Simple(predicate: QuestionnaireNodePredicate, message: String) extends Validation

  implicit val encoder: Encoder[Validation] = deriveCustomEncoder
  implicit val decoder: Decoder[Validation] = deriveCustomDecoder
}

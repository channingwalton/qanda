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
import io.questions.derivedvalues.{ DerivedValuesResult, ScriptRunner }
import io.questions.model.EncoderHelpers
import io.questions.model.questionnaire.nodekey.NodeKey

sealed trait AnswerDeriver extends ((QuestionnaireNode, NodeKey) ⇒ DerivedValuesResult) with Product with Serializable

object AnswerDeriver extends EncoderHelpers {
  case class JavaScriptAnswerDeriver(script: String, functionName: String) extends AnswerDeriver {
    def apply(questionnaire: QuestionnaireNode, currentNode: NodeKey): DerivedValuesResult =
      ScriptRunner(script, functionName, questionnaire, currentNode)
  }

  implicit val encoder: Encoder[AnswerDeriver] = deriveCustomEncoder
  implicit val decoder: Decoder[AnswerDeriver] = deriveCustomDecoder

}

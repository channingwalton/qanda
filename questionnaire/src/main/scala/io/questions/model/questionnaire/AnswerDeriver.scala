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

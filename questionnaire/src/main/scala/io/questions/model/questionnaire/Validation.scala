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

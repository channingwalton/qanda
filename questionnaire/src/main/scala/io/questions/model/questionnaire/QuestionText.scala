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

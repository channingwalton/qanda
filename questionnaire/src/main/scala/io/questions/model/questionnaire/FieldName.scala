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

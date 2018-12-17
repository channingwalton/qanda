package io.questions.derivedvalues

import cats.data.NonEmptyList
import io.circe.syntax._
import io.circe.{ Encoder, Json, JsonObject, ObjectEncoder }
import io.questions.model.questionnaire.Element.{ Derived, Enumerations, Info, Parent, Primitive }
import io.questions.model.questionnaire.PrimitiveAnswer._
import io.questions.model.questionnaire.{ Element, FieldName, PrimitiveAnswer, QuestionnaireNode }

object JsonEncoder {
  val primitiveAnswerEncoder: Encoder[PrimitiveAnswer] = {
    case s: StringAnswer =>
      s.answer.fold(Json.Null) {
        Json.fromString
      }
    case i: IntAnswer =>
      i.answer.fold(Json.Null) {
        Json.fromInt
      }
    case bd: BigDecimalAnswer =>
      bd.answer.fold(Json.Null) {
        Json.fromBigDecimal
      }
    case e: EnumerationAnswer =>
      e.answer.fold(Json.Null) {
        Json.fromString
      }
    case t: DateTimeAnswer => PrimitiveAnswer.encoder(t)
  }

  val elementEncoder: Encoder[Element] = new Encoder[Element] {
    private def fields(fs: List[(FieldName, Json)]) = Json.fromFields(fs.filterNot(_._2.isNull).map { case (fn, j) ⇒ fn.value -> j })

    private def nodeFields(qns: List[QuestionnaireNode]) = fields(qns.map(qn ⇒ qn.name → apply(qn.element)))

    private def repeating(qns: NonEmptyList[QuestionnaireNode]): Json =
      Json.fromValues {
        qns.map { q ⇒
          val j = apply(q.element)
          j.mapObject(_.+:("keyExtension" → q.key.extension.asJson))
        }.toList
      }

    override def apply(a: Element): Json = a match {
      case p: Parent       ⇒ if (p.repeating) repeating(p.children) else nodeFields(p.toList)
      case p: Primitive    ⇒ primitiveAnswerEncoder(p.answer)
      case d: Derived      ⇒ primitiveAnswerEncoder(d.answer)
      case _: Info         ⇒ Json.Null
      case _: Enumerations ⇒ Json.Null
    }
  }

  val questionnaireNodeEncoder: Encoder[QuestionnaireNode] = new ObjectEncoder[QuestionnaireNode] {
    def encodeObject(a: QuestionnaireNode): JsonObject =
      JsonObject(a.name.value → elementEncoder(a.element))
  }.mapJsonObject {
    _.filter {
      case (_, value) => !value.isNull
    }
  }

  def apply(q: QuestionnaireNode): Json = questionnaireNodeEncoder(q)
}

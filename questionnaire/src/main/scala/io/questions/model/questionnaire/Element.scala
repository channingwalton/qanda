package io.questions.model.questionnaire

import cats.data.NonEmptyList
import io.circe.{ Decoder, Encoder }
import io.questions.model.EncoderHelpers

sealed trait Element extends Product with Serializable {
  def blank: Element
}

object Element extends EncoderHelpers {
  final case class Parent(repeating: Boolean, children: NonEmptyList[QuestionnaireNode]) extends Element {
    override def blank: Element                               = Parent(repeating, children.map(_.blank))
    def map(f: QuestionnaireNode ⇒ QuestionnaireNode): Parent = Parent(repeating, children.map(f))
    def toList: List[QuestionnaireNode]                       = children.toList
  }

  final case class Primitive(answer: PrimitiveAnswer) extends Element {
    override def blank: Element = Primitive(answer.blank)
  }

  final case class Derived(answer: PrimitiveAnswer, deriver: AnswerDeriver) extends Element {
    def blank: Element = Derived(answer.blank, deriver)
  }

  final case class Info(text: String) extends Element {
    override def blank: Element = this
  }

  final case class Enumerations(enums: Map[EnumerationName, NonEmptyList[EnumerationValue]]) extends Element {
    override def blank: Element = this
  }

  def NonRepeatingParent(child: QuestionnaireNode, children: QuestionnaireNode*): Parent =
    Parent(repeating = false, NonEmptyList.of(child, children: _*))

  def RepeatingParent(child: QuestionnaireNode): Parent =
    Parent(repeating = true, NonEmptyList.of(child))

  implicit val encoder: Encoder[Element] = deriveCustomEncoder
  implicit val decoder: Decoder[Element] = deriveCustomDecoder
}

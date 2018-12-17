package io.questions.model.questionnaire

import cats.data.EitherT
import cats.instances.list._
import cats.instances.either._
import cats.syntax.traverse._
import cats.data.NonEmptyList
import cats.syntax.eq._
import cats.syntax.show._
import io.questions.model.questionnaire.Element.{ Derived, Enumerations, Info, Parent, Primitive }
import io.questions.model.questionnaire.nodekey.NodeKey

object QuestionnaireNodeAnswerUpdater {
  def apply(node: QuestionnaireNode, aKey: NodeKey, value: PrimitiveAnswer): Either[String, QuestionnaireNode] =
    if (aKey === node.key) updateElement(node, value)
    else findAndUpdateChild(node, aKey, value)

  private def updateElement(node: QuestionnaireNode, leaf: PrimitiveAnswer): Either[String, QuestionnaireNode] = {
    def updateIfOk(current: Primitive): Either[String, QuestionnaireNode] =
      if (current.answer.getClass == leaf.getClass)
        Right(node.copy(element = current.copy(answer = leaf)))
      else Left(s"Type mismatch for node ${node.key.show}. Expected ${current.getClass} but got ${leaf.getClass}")

    node.element match {
      case _: Parent    ⇒ Left(s"Type mismatch for node ${node.key.show}. Cannot update an answer for an Element.Parent")
      case p: Primitive ⇒ updateIfOk(p)
      case _: Derived ⇒
        Left(s"Type mismatch for node ${node.key.show}. Cannot update an answer for an Element.Derived")
      case _: Info ⇒
        Left(s"Type mismatch for node ${node.key.show}. Cannot update an answer for an Element.Info")
      case _: Enumerations ⇒
        Left(s"Type mismatch for node ${node.key.show}. Cannot update an answer for an Element.Enumerations")
    }
  }

  private def findAndUpdateChild(node: QuestionnaireNode, aKey: NodeKey, value: PrimitiveAnswer): Either[String, QuestionnaireNode] =
    node.element match {
      case p: Parent       ⇒ update(node, p, aKey, value)
      case _: Primitive    ⇒ Right(node)
      case _: Derived      ⇒ Right(node)
      case _: Info         ⇒ Right(node)
      case _: Enumerations ⇒ Right(node)
    }

  private def update(node: QuestionnaireNode, parent: Parent, aKey: NodeKey, value: PrimitiveAnswer): Either[String, QuestionnaireNode] = {
    val kids = EitherT[List, String, QuestionnaireNode](parent.toList.map(Right(_)))
    val res  = kids.flatMap(k ⇒ EitherT.fromEither[List](apply(k, aKey, value)))

    val res2: Either[String, List[QuestionnaireNode]] = res.value.sequence[Either[String, ?], QuestionnaireNode]

    res2.map(kids ⇒ node.copy(element = parent.copy(children = NonEmptyList.fromListUnsafe(kids))))
  }
}

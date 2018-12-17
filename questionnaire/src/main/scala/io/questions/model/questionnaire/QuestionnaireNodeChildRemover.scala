package io.questions.model.questionnaire

import cats.syntax.eq._
import cats.syntax.show._
import cats.data.NonEmptyList
import io.questions.model.questionnaire.Element.Parent
import io.questions.model.questionnaire.nodekey.NodeKey

object QuestionnaireNodeChildRemover {
  def apply(rootNode: QuestionnaireNode, repeatingParentKey: NodeKey, childNodeKey: NodeKey): Either[String, QuestionnaireNode] =
    for {
      parent ← rootNode.find(repeatingParentKey)
      _      ← isValidChild(parent, childNodeKey)
    } yield removeChild(rootNode, repeatingParentKey, childNodeKey)

  private def isValidChild(parent: QuestionnaireNode, childNodeKey: NodeKey): Either[String, Unit] =
    parent.element match {
      case p: Parent if p.children.exists(_.key === childNodeKey) ⇒
        Right(())
      case _ ⇒
        Left(s"Node with key ${childNodeKey.show} doesn't belong to parent ${parent.key.show} or it is the only child for that node")
    }

  private def removeChild(current: QuestionnaireNode, repeatingParentKey: NodeKey, childNodeKey: NodeKey): QuestionnaireNode =
    current.element match {
      case p: Parent if current.key === repeatingParentKey && p.children.exists(_.key === childNodeKey) ⇒
        val filtered = p.children.filterNot(_.key === childNodeKey)
        // if we can't build a non-empty list as we removed the last node, we blank the element in the remaining children instead
        val updatedChildren = NonEmptyList.fromList(filtered).getOrElse(p.children.map(_.blank))
        current.copy(element = p.copy(children = updatedChildren))

      case p: Parent ⇒
        current.copy(element = p.copy(children = p.children.map(removeChild(_, repeatingParentKey, childNodeKey))))

      case _ ⇒ current
    }

}

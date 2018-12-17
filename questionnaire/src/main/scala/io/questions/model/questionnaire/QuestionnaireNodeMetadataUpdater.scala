package io.questions.model.questionnaire

import cats.syntax.eq._
import io.questions.model.questionnaire.Element.Parent
import io.questions.model.questionnaire.nodekey.NodeKey

object QuestionnaireNodeMetadataUpdater {
  def apply(rootNode: QuestionnaireNode, targetNodeKey: NodeKey, newMetadata: Seq[NodeMetadata]): Either[String, QuestionnaireNode] =
    for {
      _ ← rootNode.find(targetNodeKey)
    } yield updateMetadata(rootNode, targetNodeKey, newMetadata)

  private def updateMetadata(current: QuestionnaireNode, targetNodeKey: NodeKey, newMetadata: Seq[NodeMetadata]): QuestionnaireNode =
    current.element match {
      case _ if current.key === targetNodeKey ⇒
        current.copy(metadata = newMetadata)
      case p: Parent ⇒
        current.copy(element = p.copy(children = p.children.map(updateMetadata(_, targetNodeKey, newMetadata))))
      case _ ⇒ current
    }

}

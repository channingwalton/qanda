package io.questions.model.questionnaire

import cats.data.NonEmptyList
import cats.syntax.eq._
import io.questions.model.questionnaire.Element.{ Derived, Enumerations, Info, Parent, Primitive }
import io.questions.model.questionnaire.nodekey.{ NodeKey, NodeKeyExtension }

object QuestionnaireNodeChildAppender {
  def apply(rootNode: QuestionnaireNode, repeatingParentKey: NodeKey, extension: NodeKeyExtension): Either[String, QuestionnaireNode] =
    for {
      parent   ← rootNode.find(repeatingParentKey)
      children ← parent.getRepeatingChildNodes
    } yield {
      val childTemplate = reduceToTemplate(children.head)
      val keyMap        = childTemplate.collectKeys.map(k ⇒ (k, k.copy(extension = extension))).toMap
      val newChild      = replaceKeys(childTemplate, keyMap)
      addChild(rootNode, repeatingParentKey, newChild)
    }

  private def reduceToTemplate(n: QuestionnaireNode): QuestionnaireNode =
    n.copy(
        element = n.element match {
          case p: Parent if p.repeating ⇒ Element.RepeatingParent(reduceToTemplate(p.children.head))
          case p: Parent                ⇒ p.copy(children = p.children.map(reduceToTemplate))
          case p: Primitive             ⇒ p
          case d: Derived               ⇒ d
          case d: Info                  ⇒ d
          case d: Enumerations          ⇒ d
        }
      )
      .blank

  def addChild(current: QuestionnaireNode, repeatingParentKey: NodeKey, child: QuestionnaireNode): QuestionnaireNode =
    current.element match {
      case p: Parent if current.key === repeatingParentKey ⇒
        current.copy(element = p.copy(children = p.children ::: NonEmptyList.of(child)))

      case p: Parent ⇒
        current.copy(element = p.copy(children = p.children.map(addChild(_, repeatingParentKey, child))))

      case _: Primitive    ⇒ current
      case _: Derived      ⇒ current
      case _: Info         ⇒ current
      case _: Enumerations ⇒ current
    }

  private def replaceKeys(node: QuestionnaireNode, keyMap: Map[NodeKey, NodeKey]): QuestionnaireNode =
    node.copy(
      key = keyMap(node.key),
      element = node.element match {
        case p: Parent       ⇒ p.copy(children = p.children.map(replaceKeys(_, keyMap)))
        case p: Primitive    ⇒ p
        case d: Derived      ⇒ d
        case a: Info         ⇒ a
        case a: Enumerations ⇒ a
      }
    )
}

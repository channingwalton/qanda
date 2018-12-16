/*
 * Copyright 2018 TBD
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.questions.model

import cats.instances.int._
import cats.syntax.eq._
import cats.syntax.show._
import io.questions.model.questionnaire.Element.{ Derived, Enumerations, Info, Parent, Primitive }
import io.questions.model.questionnaire.PrimitiveAnswer.StringAnswer
import io.questions.model.questionnaire.nodekey.NodeKey
import io.questions.util.TypedUUID

package object questionnaire {
  type PredicateResult = Either[String, Boolean]

  implicit class PathUtils(root: QuestionnaireNode) {
    def /(name: FieldName): QuestionnaireNode =
      root.element match {
        case p: Parent if p.repeating =>
          throw new IllegalArgumentException(show"${root.name} is a repeating Parent. Can't descend to $name")
        case p: Parent =>
          p.children
            .find(_.name === name)
            .getOrElse(throw new IllegalArgumentException(show"${root.name} has no element with keyBase $name"))
        case _: Primitive => throw new IllegalArgumentException(show"${root.name} is a Primitive. Can't descend to $name")
        case _: Derived   => throw new IllegalArgumentException(show"${root.name} is a Derived. Can't descend to $name")
        case _: Info      => throw new IllegalArgumentException(show"${root.name} is an Info. Can't descend to $name")
      }

    def /-/(name: FieldName): QuestionnaireNode = {
      def recurse(n: QuestionnaireNode): Option[QuestionnaireNode] =
        n.element match {
          case p: Parent if p.repeating => None
          case p: Parent =>
            p.children.find(_.name === name) orElse p.children.foldLeft(Option.empty[QuestionnaireNode]) {
              case (acc, node) ⇒
                acc orElse recurse(node)
            }
          case _: Primitive    => None
          case _: Derived      => None
          case _: Info         => None
          case _: Enumerations ⇒ None
        }

      root.element match {
        case p: Parent if p.repeating =>
          throw new IllegalArgumentException(show"${root.name} is a repeating Parent. Can't descend to $name")
        case _: Parent =>
          recurse(root).getOrElse(throw new IllegalArgumentException(show"${root.key} has no element with keyBase $name"))
        case _: Primitive    => throw new IllegalArgumentException(show"${root.key} is a Primitive. Can't descend to $name")
        case _: Derived      => throw new IllegalArgumentException(show"${root.key} is a Derived. Can't descend to $name")
        case _: Info         => throw new IllegalArgumentException(show"${root.key} is an Info. Can't descend to $name")
        case _: Enumerations ⇒ throw new IllegalArgumentException(show"${root.key} is an Enumerations. Can't descend to $name")
      }
    }

    def /(index: Int): QuestionnaireNode = root.element match {
      case p: Parent if p.repeating => p.toList(index)
      case _: Parent                => throw new IllegalArgumentException(show"${root.name} is a non-repeating Parent. Can't get element at index $index")
      case _: Primitive             => throw new IllegalArgumentException(show"${root.name} is a Primitive. Can't get element at index $index")
      case _: Derived               => throw new IllegalArgumentException(show"${root.name} is a Derived. Can't get element at index $index")
      case _: Info                  => throw new IllegalArgumentException(show"${root.name} is an Info. Can't get element at index $index")
      case _: Enumerations          => throw new IllegalArgumentException(show"${root.name} is an Enumerations. Can't get element at index $index")
    }

    def answerByPath(p: QuestionnaireNode ⇒ QuestionnaireNode, answer: PrimitiveAnswer): Either[String, QuestionnaireNode] =
      root.answer(p(root).key, answer)
  }

  implicit class MetaUtils(questionnaireNode: QuestionnaireNode) {
    def getId: Either[String, QuestionnaireId] =
      questionnaireNode
        .getAnswer(Components.idKey)
        .flatMap {
          case StringAnswer(answer) => answer.toRight("No Key value")
          case x                    ⇒ Left(s"The type of the ID is incorrect, expected StringAnswer but got ${x.getClass.getSimpleName}")
        }
        .flatMap { QuestionnaireId.fromString }
  }

  type QuestionnaireId = TypedUUID[QuestionnaireNode]

  object QuestionnaireId {
    def unapply(uuid: String): Option[QuestionnaireId] = TypedUUID.unapply(uuid)
    def random: QuestionnaireId                        = TypedUUID.random()
    def from[T](id: TypedUUID[T]): QuestionnaireId     = TypedUUID[QuestionnaireNode](id.id)
    def fromString(uuid: String): Either[String, QuestionnaireId] =
      TypedUUID.fromString(uuid, s"Not a valid QuestionnaireId: $uuid")
  }

  implicit class QuestionnaireNodeSyntax(n: QuestionnaireNode) {
    def readOnly: QuestionnaireNode                                                 = n.copy(editability = QuestionnaireNodePredicate.False)
    def makeOptional: QuestionnaireNode                                             = n.copy(optional = QuestionnaireNodePredicate.True)
    def hide: QuestionnaireNode                                                     = n.copy(visibility = QuestionnaireNodePredicate.False)
    def visibleWhen(p: QuestionnaireNodePredicate): QuestionnaireNode               = n.copy(visibility = p)
    def validate(message: String, p: QuestionnaireNodePredicate): QuestionnaireNode = n.copy(validation = Validation.Simple(p, message))
  }

  def nonRepeatingParentQuestion(fieldName: FieldName,
                                 text: QuestionText,
                                 first: QuestionnaireNode,
                                 rest: QuestionnaireNode*): QuestionnaireNode =
    QuestionnaireNode(NodeKey.random, fieldName, text, Element.NonRepeatingParent(first, rest: _*))

  def repeatingParentQuestion(fieldName: FieldName, text: QuestionText, repeatingNode: QuestionnaireNode): QuestionnaireNode =
    QuestionnaireNode(NodeKey.random, fieldName, text, Element.RepeatingParent(repeatingNode))
}

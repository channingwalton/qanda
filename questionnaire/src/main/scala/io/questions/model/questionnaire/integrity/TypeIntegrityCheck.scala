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

package io.questions.model.questionnaire.integrity

import io.questions.model.questionnaire.Element.{ Derived, Enumerations, Info, Parent, Primitive }
import io.questions.model.questionnaire.{ AncestorList, PrimitiveAnswer, QuestionnaireNode, QuestionnaireNodePredicate }
import io.questions.model.questionnaire.QuestionnaireNodePredicate._
import io.questions.model.questionnaire.Validation.{ AlwaysValid, Simple }
import io.questions.model.questionnaire.nodekey.NodeKey
import io.questions.util.collection.{ MapMonoid, SetMonoid }
import shapeless.syntax.typeable._

object TypeIntegrityCheck {
  private val noErrors = Set.empty[IntegrityError]

  // scalastyle:off
  def apply(root: QuestionnaireNode): IntegrityErrorSet = {
    val types = answersByKey(root)

    def checkNode(nodeToCheck: QuestionnaireNode): IntegrityErrorSet = {
      def checkPredicate(currentNode: QuestionnaireNode, predicate: QuestionnaireNodePredicate, label: String): IntegrityErrorSet =
        predicate match {
          case False            ⇒ noErrors
          case True             ⇒ noErrors
          case AnswerExists     ⇒ noErrors
          case Not(p)           ⇒ checkPredicate(currentNode, p, label)
          case And(left, right) ⇒ checkPredicate(currentNode, left, label) ++ checkPredicate(currentNode, right, label)
          case Or(left, right)  ⇒ checkPredicate(currentNode, left, label) ++ checkPredicate(currentNode, right, label)

          case IsAlphabetic ⇒
            types.get(currentNode.key).fold(noErrors) { pa ⇒
              pa.cast[PrimitiveAnswer.StringAnswer]
                .map(_ ⇒ noErrors)
                .getOrElse(
                  Set(IntegrityError.PredicateTypeMismatch(nodeToCheck.key, label, PrimitiveAnswer.StringAnswer(None), currentNode.key, pa))
                )
            }

          case Exists(p) ⇒
            currentNode.getRepeatingChildNodes.fold(
              _ ⇒ noErrors,
              _.toList.foldLeft(noErrors) { case (acc, n) ⇒ acc ++ checkPredicate(n, p, label) }
            )

          case ExistsExactlyOne(p) ⇒
            currentNode.getRepeatingChildNodes.fold(
              _ ⇒ noErrors,
              _.toList.foldLeft(noErrors) { case (acc, n) ⇒ acc ++ checkPredicate(n, p, label) }
            )

          case ForAll(p) ⇒
            currentNode.getRepeatingChildNodes.fold(
              _ ⇒ noErrors,
              _.toList.foldLeft(noErrors) { case (acc, n) ⇒ acc ++ checkPredicate(n, p, label) }
            )

          case NodeSelector(path, p) ⇒
            path(AncestorList(root))
              .fold(
                _ ⇒ noErrors,
                ancestors ⇒ checkPredicate(ancestors.current, p, label)
              )

          case AnswerEquals(answer) ⇒
            types.get(currentNode.key).fold(noErrors) { pa =>
              if (PrimitiveAnswer.typeCheck(answer, pa).isLeft)
                Set(IntegrityError.PredicateTypeMismatch(nodeToCheck.key, label, answer, currentNode.key, pa))
              else noErrors
            }
        }

      def checkValidation(node: QuestionnaireNode) = node.validation match {
        case AlwaysValid                => noErrors
        case Simple(predicate, message) => checkPredicate(node, predicate, s"validation '$message'")
      }

      checkPredicate(nodeToCheck, nodeToCheck.optional, "optional") ++
      checkPredicate(nodeToCheck, nodeToCheck.visibility, "visibility") ++
      checkPredicate(nodeToCheck, nodeToCheck.editability, "editability") ++
      checkValidation(nodeToCheck)
    }

    root.reduce(checkNode)
  }

  private def answersByKey(questionnaireNode: QuestionnaireNode) =
    questionnaireNode.reduce(
      qn ⇒
        qn.element match {
          case p: Primitive    => Map(qn.key -> p.answer)
          case d: Derived      => Map(qn.key -> d.answer)
          case _: Parent       => Map.empty[NodeKey, PrimitiveAnswer]
          case _: Info         => Map.empty[NodeKey, PrimitiveAnswer]
          case _: Enumerations => Map.empty[NodeKey, PrimitiveAnswer]
      }
    )
}

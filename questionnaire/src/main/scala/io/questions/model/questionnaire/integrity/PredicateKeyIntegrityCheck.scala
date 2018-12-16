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

import cats.data.NonEmptySet
import io.questions.model.questionnaire.{ QuestionnaireNode, QuestionnaireNodePredicate, Validation }
import io.questions.model.questionnaire.Validation.{ AlwaysValid, Simple }
import io.questions.model.questionnaire.nodekey.NodeKeyBase
import io.questions.util.collection.SetMonoid

import scala.collection.immutable.SortedSet

object PredicateKeyIntegrityCheck {
  def apply(questionnaireNode: QuestionnaireNode): IntegrityErrorSet = {
    val keyBases = questionnaireNode.collectKeys.map(_.base)

    def checkNode(node: QuestionnaireNode): IntegrityErrorSet = {
      def checkPredicate(predicate: QuestionnaireNodePredicate, label: String): IntegrityErrorSet =
        NonEmptySet
          .fromSet[NodeKeyBase](SortedSet[NodeKeyBase](predicate.keyBases.filterNot(keyBases).toSeq: _*))
          .map { missing ⇒
            IntegrityError.PredicateRefersToNonExistantKey(node.key, label, missing)
          }
          .toSet

      def checkValidation(v: Validation) = v match {
        case AlwaysValid                => Set.empty
        case Simple(predicate, message) => checkPredicate(predicate, s"validation '$message'")
      }

      checkPredicate(node.optional, "optional") ++
      checkPredicate(node.visibility, "visibility") ++
      checkPredicate(node.editability, "editability") ++
      checkValidation(node.validation)
    }

    questionnaireNode.reduce(checkNode)
  }
}

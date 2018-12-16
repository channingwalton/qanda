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

import cats.Show
import cats.instances.string.catsKernelStdOrderForString
import cats.syntax.show._
import cats.data.NonEmptySet
import io.questions.model.questionnaire.nodekey.{ NodeKey, NodeKeyBase }
import io.questions.model.questionnaire.PrimitiveAnswer

sealed trait IntegrityError extends Product with Serializable

object IntegrityError {
  case class PredicateRefersToNonExistantKey(node: NodeKey, label: String, missingKeys: NonEmptySet[NodeKeyBase]) extends IntegrityError

  case class PredicateTypeMismatch(node: NodeKey, label: String, expected: PrimitiveAnswer, referent: NodeKey, actual: PrimitiveAnswer)
      extends IntegrityError

  case class NonUniqueKey(keys: NonEmptySet[NodeKey]) extends IntegrityError

  implicit val show: Show[IntegrityError] = new Show[IntegrityError] {
    override def show(t: IntegrityError): String = t match {
      case PredicateRefersToNonExistantKey(nodeKey, label, missingKeys) =>
        s"""|Non-Existent Key: QuestionnaireNode ${nodeKey.show}'s $label predicate refers to the following unknown keys:
            |\t${missingKeys.map(_.show).toSortedSet.mkString(",")}""".stripMargin

      case PredicateTypeMismatch(node, label, expected, referent, actual) =>
        s"""|Type Mismatch: QuestionnaireNode ${node.show}'s $label predicate
            |\tExpected type ${expected.getClass.getSimpleName} at questionnaire node ${referent.show}
            |\tGot ${actual.getClass.getSimpleName}.""".stripMargin

      case NonUniqueKey(keys) =>
        s"Non-Unique Keys:${System.getProperty("line.separator")}\t" +
        keys.map(_.show).toSortedSet.mkString(System.getProperty("line.separator") + "\t")
    }
  }
}

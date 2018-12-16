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
import io.questions.model.questionnaire.QuestionnaireNode
import io.questions.model.questionnaire.nodekey.NodeKey

import scala.collection.immutable.SortedSet

object UniqueKeyIntegrityCheck {
  def apply(questionnaireNode: QuestionnaireNode): IntegrityErrorSet = {
    // _1 contains keys found so far, _2 contains duplicates
    def check(s: (Set[NodeKey], Set[NodeKey]), qn: QuestionnaireNode): (Set[NodeKey], Set[NodeKey]) =
      if (s._1.contains(qn.key)) (s._1, s._2 + qn.key) else (s._1 + qn.key, s._2)

    val duplicates = questionnaireNode
      .foldLeft((Set.empty[NodeKey], Set.empty[NodeKey]))(check)
      ._2

    NonEmptySet
      .fromSet(SortedSet(duplicates.toSeq: _*))
      .map(keys ⇒ IntegrityError.NonUniqueKey(keys))
      .toSet
  }
}

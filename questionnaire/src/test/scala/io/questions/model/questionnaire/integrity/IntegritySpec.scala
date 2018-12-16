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
import io.questions.QuestionsSpec
import io.questions.model.questionnaire.Path._
import io.questions.model.questionnaire.QuestionnaireNodePredicate._
import io.questions.model.questionnaire.nodekey.{ NodeKey, NodeKeyBase, NodeKeyExtension }
import io.questions.model.questionnaire.{ PrimitiveAnswer, QuestionnaireNode }

class IntegritySpec extends QuestionsSpec {
  private val l3TemplateKey = NodeKey.random
  private val questionnaire: QuestionnaireNode =
    qn("root", qn("l1", "ok"), rqn("l2", qn(l3TemplateKey, "foo"), qn(l3TemplateKey.base, NodeKeyExtension.random, "bar")))

  "reports no integrity errors for nodes without optional predicates or validation" in {
    questionnaire.checkIntegrity mustBe Set.empty
  }

  "report duplicate keys" - {
    "when there are none" in {
      questionnaire.checkIntegrity mustBe Set.empty
    }

    "when there is a duplicate" in {
      val key           = NodeKey.random
      val questionnaire = qn("root", qn(key, "ok"), qn(key, "oops"))
      questionnaire.checkIntegrity mustBe Set(IntegrityError.NonUniqueKey(NonEmptySet.of(key)))
    }
  }

  "reports all integrity errors for predicates referring to missing keys" in {
    val fooKeyBase   = NodeKeyBase.random
    val barKeyBase   = NodeKeyBase.random
    val fubarKeyBase = NodeKeyBase.random
    val qn1Key       = NodeKey.random
    val qn2Key       = NodeKey.random

    val qn1 = qn(qn1Key, root / fooKeyBase isAnswered, root / fooKeyBase isAnswered)
    val qn2 = qn(qn2Key,
                 And(root / qn1Key.base isAnswered, Or(root / qn2Key.base isAnswered, root / barKeyBase isAnswered)),
                 root / fubarKeyBase isAnswered)

    val withChecks = qn("root", qn1, qn2)

    withChecks.checkIntegrity mustBe
    Set(
      IntegrityError.PredicateRefersToNonExistantKey(qn1Key, "optional", NonEmptySet.of(fooKeyBase)),
      IntegrityError.PredicateRefersToNonExistantKey(qn1Key, "validation 'foo'", NonEmptySet.of(fooKeyBase)),
      IntegrityError.PredicateRefersToNonExistantKey(qn2Key, "optional", NonEmptySet.of(barKeyBase)),
      IntegrityError.PredicateRefersToNonExistantKey(qn2Key, "validation 'foo'", NonEmptySet.of(fubarKeyBase))
    )
  }

  "report type errors for predicates" in {
    val qn1Key = NodeKey.random
    val qn2Key = NodeKey.random

    val qn1 = qn(qn1Key, "answer")
    val qn2 = qn(qn2Key, root / qn1Key.base === "" && (root / qn2Key.base === "" || root / qn2Key.base === 1), root / qn1Key.base === 1)

    val withChecks = qn("root", qn1, qn2)

    withChecks.checkIntegrity mustBe
    Set(
      IntegrityError.PredicateTypeMismatch(qn2Key,
                                           "optional",
                                           PrimitiveAnswer.IntAnswer(Some(1)),
                                           qn2Key,
                                           PrimitiveAnswer.StringAnswer(Some(""))),
      IntegrityError.PredicateTypeMismatch(qn2Key,
                                           "validation 'foo'",
                                           PrimitiveAnswer.IntAnswer(Some(1)),
                                           qn1Key,
                                           PrimitiveAnswer.StringAnswer(Some("answer")))
    )
  }
}

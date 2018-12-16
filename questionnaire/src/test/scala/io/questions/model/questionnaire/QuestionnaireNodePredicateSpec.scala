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

package io.questions.model.questionnaire

import io.circe.parser.parse
import io.circe.syntax._
import io.questions.QuestionsSpec
import io.questions.model.questionnaire.Path._
import io.questions.model.questionnaire.QuestionnaireNodePredicate._
import io.questions.model.questionnaire.TestQuestionnaire._
import io.questions.model.questionnaire.nodekey.{ NodeKey, NodeKeyBase, NodeKeyExtension }

class QuestionnaireNodePredicateSpec extends QuestionsSpec {
  "will serialise" in {
    val pred = (root / homeAddresses.key.base Ǝ (randomRelativePath === "Bisley")) &&
    ((root / countriesOfNationality.key.base) ∀ (root / countryOfNationality.key.base =!= "Moon"))
    val json = pred.asJson
    val text = json.spaces2

    val recovered = parse(text).flatMap(_.as[QuestionnaireNodePredicate]).right.value

    recovered mustEqual pred
  }

  private val l3TemplateKey  = NodeKey.random
  private val l3RelativePath = relative / l3TemplateKey.base

  private val questionnaire: QuestionnaireNode =
    qn("root", qn("l1", "ok"), qn("l2", qn(l3TemplateKey, "foo"), qn(l3TemplateKey.base, NodeKeyExtension.random, "bar")))

  private val l2Key  = questionnaire /-/ "l2" key
  private val l2Path = root / l2Key.base

  private val randomRelativePath = relative / NodeKeyBase.random

  "exists" - {
    "when exists" in {
      (l2Path Ǝ (l3RelativePath === "foo"))(questionnaire) mustBe Right(true)
      (l2Path Ǝ (l3RelativePath === "bar"))(questionnaire) mustBe Right(true)
    }

    "when does not exist" in {
      val pred = l2Path Ǝ (l3RelativePath === "foobar")
      pred(questionnaire) mustBe Right(false)
    }

    "when key does not exist" in {
      val pred = l2Path Ǝ (randomRelativePath === "foobar")
      pred(questionnaire) mustBe Right(false)
    }
  }

  "exists exactly once" - {
    "when only one exists" in {
      (l2Path Ǝ1 (l3RelativePath === "foo"))(questionnaire) mustBe Right(true)
    }

    "when more than one exists" in {
      (l2Path Ǝ1 (l3RelativePath isAnswered))(questionnaire) mustBe Right(false)
    }

    "when does not exist" in {
      val pred = l2Path Ǝ (l3RelativePath === "foobar")
      pred(questionnaire) mustBe Right(false)
    }

    "when key does not exist" in {
      val pred = l2Path Ǝ (randomRelativePath === "foobar")
      pred(questionnaire) mustBe Right(false)
    }
  }

  "forAll" - {
    "when true" in {
      val pred = l2Path ∀ (l3RelativePath isAnswered)
      pred(questionnaire) mustBe Right(true)
    }

    "when false" in {
      val pred = l2Path ∀ (l3RelativePath === "foobar")
      pred(questionnaire) mustBe Right(false)
    }

    "when key does not exist" in {
      val pred = l2Path ∀ (randomRelativePath === "foobar")
      pred(questionnaire) mustBe Right(false)
    }
  }
}

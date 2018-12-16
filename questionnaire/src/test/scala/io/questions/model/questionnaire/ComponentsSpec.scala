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

import cats.data.NonEmptyList
import io.questions.QuestionsSpec
import io.questions.model.questionnaire.Element.Parent
import io.questions.model.questionnaire.nodekey.NodeKey
import io.questions.testdata.ExampleComponents

class ComponentsSpec extends QuestionsSpec {

  "standard" - {
    val questionnaireId = QuestionnaireId.random
    val toTest: QuestionnaireNode =
      ExampleComponents.standard(NodeKey.random, "root", "Root", Parent(repeating = false, NonEmptyList.of(qn("l0", "foo"))))
    "adds metadata node to the provided Parent node" in {
      toTest.find(ExampleComponents.metadata(questionnaireId).key).isRight mustBe true
    }
    "metadata node contains id node" in {
      toTest.find(ExampleComponents.metadata(questionnaireId).key).flatMap(_.find(Components.idKey)).isRight mustBe true
    }
  }
}

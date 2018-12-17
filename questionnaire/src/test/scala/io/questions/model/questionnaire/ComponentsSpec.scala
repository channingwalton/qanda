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

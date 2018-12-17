package io.questions.model.questionnaire

import cats.data.NonEmptyList
import cats.syntax.show._
import io.questions.QuestionsSpec
import io.questions.model.questionnaire.Element.{ Parent, Primitive }
import io.questions.model.questionnaire.PrimitiveAnswer.{ IntAnswer, StringAnswer }
import io.questions.model.questionnaire.nodekey.NodeKey
import io.questions.testdata.ExampleComponents

class PackageObjectSpec extends QuestionsSpec {

  private val nodes: QuestionnaireNode = qn("l2", qn("l3", "foo"), qn("l3", "bar"))

  private val questionnaire: QuestionnaireNode =
    ExampleComponents
      .standard(NodeKey.random, FieldName("root"), QuestionText("Root node"), Parent(repeating = false, NonEmptyList.of(nodes)))

  "MetaUtils" - {
    "getId" - {
      "if given node has no id node, it returns an error" in {
        nodes.getId.left.value must startWith(s"Key ${Components.idKey.show} is unknown")
      }
      "if the id node has no value, it returns an error" in {
        val noAnswerQuestionnaire =
          questionnaire
            .modifyAnswer(Components.idKey, _ ⇒ StringAnswer(None))
            .getOrElse(fail("Couldn't modify the value of id node"))
        noAnswerQuestionnaire.getId.left.value mustBe "No Key value"
      }
      "if the node with key for id has the wrong type, it returns an error" in {
        val badTypeQuestionnaire =
          QuestionnaireNode(Components.idKey, FieldName("badType"), QuestionText("Bad type"), Primitive(IntAnswer(None)))
        badTypeQuestionnaire.getId.left.value mustBe "The type of the ID is incorrect, expected StringAnswer but got IntAnswer"
      }
      "if the id cannot be parsed as a SubjectId, it returns an error" in {
        val noAnswerQuestionnaire =
          questionnaire
            .modifyAnswer(Components.idKey, _ ⇒ StringAnswer(Some("1234")))
            .getOrElse(fail("Couldn't modify the value of id node"))
        noAnswerQuestionnaire.getId.left.value mustBe "Not a valid QuestionnaireId: 1234"
      }
      "returns the value in the id node" in {
        val idNode   = questionnaire.find(Components.idKey).getOrElse(fail("Couldn't find id node"))
        val idAnswer = idNode.element.asInstanceOf[Primitive].answer.asInstanceOf[StringAnswer].answer.get
        questionnaire.getId.right.value.id.toString mustBe idAnswer
      }
    }
  }
}

package io.questions.model.questionnaire

import cats.syntax.show._
import io.questions.QuestionsSpec
import io.questions.model.questionnaire.Element._
import io.questions.model.questionnaire.nodekey.NodeKey

class QuestionnaireNodeChildSpec extends QuestionsSpec {

  private val children1 = qn("child1", "answer1")
  private val children2 = qn("child2", "answer2")
  private val children3 = qn("child3", "answer3")
  private val children4 = qn("child4", "answer4")
  private val children5 = qn("child5", "answer5")
  private val group     = qn("group", children4, children5)
  private val group2    = group.copy(key = NodeKey.random)
  private val parent1   = qn("parent1", children1, children2)
  private val parent2   = qn("parent2", children3)
  private val parent3   = rqn("parent3", group, group2)
  private val rootNode  = qn("rootNode", parent1, parent2, parent3)

  "doesn't remove the node if" - {

    "the parent id doesn't exist" in {
      val badKey = NodeKey.random
      QuestionnaireNodeChildRemover(rootNode, badKey, children1.key).left.value must startWith(s"Key ${badKey.show} is unknown")
    }
    "the child id doesn't exist" in {
      val badKey   = NodeKey.random
      val expected = s"Node with key ${badKey.show} doesn't belong to parent ${parent1.key.show} or it is the only child for that node"
      QuestionnaireNodeChildRemover(rootNode, parent1.key, badKey).left.value mustBe expected
    }
    "the child id exists but doesn't belong to the parent we provided" in {
      val expected =
        s"Node with key ${children3.key.show} doesn't belong to parent ${parent1.key.show} or it is the only child for that node"
      QuestionnaireNodeChildRemover(rootNode, parent1.key, children3.key).left.value mustBe expected
    }
  }

  "removes the node if" - {
    "the child is the only child of the parent (replacing with a blank node)" in {
      val newParent2 = parent2.copy(element = NonRepeatingParent(children3.copy(element = children3.element.blank)))
      val expected   = rootNode.copy(element = NonRepeatingParent(parent1, newParent2, parent3))

      QuestionnaireNodeChildRemover(rootNode, parent2.key, children3.key).right.value mustBe expected
    }
    "the child belongs to the parent and there is at least another child" in {
      val newParent1 = parent1.copy(element = NonRepeatingParent(children1))
      val expected   = rootNode.copy(element = NonRepeatingParent(newParent1, parent2, parent3))

      QuestionnaireNodeChildRemover(rootNode, parent1.key, children2.key).right.value mustBe expected
    }
    "the child is a member of a repeatable group and not the last child" in {
      val newParent3 = parent3.copy(element = RepeatingParent(group))
      val expected   = rootNode.copy(element = NonRepeatingParent(parent1, parent2, newParent3))

      QuestionnaireNodeChildRemover(rootNode, parent3.key, group2.key).right.value mustBe expected
    }
    "the child is the only child of a repeatable group" in {
      val newParent3 = parent3.copy(
        element = RepeatingParent(
          group.copy(
            element =
              NonRepeatingParent(children4.copy(element = children4.element.blank), children5.copy(element = children5.element.blank))
          )
        )
      )
      val expected = rootNode.copy(element = NonRepeatingParent(parent1, parent2, newParent3))

      val firstRemoval = QuestionnaireNodeChildRemover(rootNode, parent3.key, group2.key).right.value
      QuestionnaireNodeChildRemover(firstRemoval, parent3.key, group.key).right.value mustBe expected
    }
  }
}

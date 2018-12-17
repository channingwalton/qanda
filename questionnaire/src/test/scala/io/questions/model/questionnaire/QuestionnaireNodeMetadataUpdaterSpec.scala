package io.questions.model.questionnaire

import cats.syntax.show._
import io.questions.QuestionsSpec
import io.questions.model.questionnaire.Element._
import io.questions.model.questionnaire.NodeMetadata._
import io.questions.model.questionnaire.nodekey.NodeKey

class QuestionnaireNodeMetadataUpdaterSpec extends QuestionsSpec {

  private val children1 = qn("child1", "answer1")
  private val children2 = qn("child2", "answer2")
  private val children3 = qn("child3", "answer3")
  private val parent1   = qn("parent1", children1, children2)
  private val parent2   = qn("parent2", children3)
  private val root      = qn("root", parent1, parent2)

  "update metadata" - {

    "doesn't update metadata if the given id doesn't exist in the tree" in {
      val badKey = NodeKey.random
      QuestionnaireNodeMetadataUpdater(root, badKey, Seq.empty[NodeMetadata]).left.value must startWith(s"Key ${badKey.show} is unknown")
    }
    "updates metadata" - {
      val newMetadata = Seq(SectionTag, PageTag, MoneyQuestion)
      "if node exists and the new metadata fully replaces the old one, not modifying the rest of the tree" in {
        val newChildren1 = children1.copy(metadata = newMetadata)
        val newParent1   = parent1.copy(element = NonRepeatingParent(newChildren1, children2))
        val expected     = root.copy(element = NonRepeatingParent(newParent1, parent2))

        QuestionnaireNodeMetadataUpdater(root, children1.key, newMetadata).right.value mustBe expected
      }
      "updates metadata for root node" in {
        val expected = root.copy(metadata = newMetadata)

        QuestionnaireNodeMetadataUpdater(root, root.key, newMetadata).right.value mustBe expected
      }
      "updates metadata for intermediate node" in {
        val newParent2 = parent2.copy(metadata = newMetadata)
        val expected   = root.copy(element = NonRepeatingParent(parent1, newParent2))

        QuestionnaireNodeMetadataUpdater(root, parent2.key, newMetadata).right.value mustBe expected
      }
    }
  }
}

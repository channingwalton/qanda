package io.questions.model.questionnaire

import cats.syntax.either._
import io.questions.QuestionsSpec
import io.questions.model.questionnaire.Path._
import io.questions.model.questionnaire.nodekey.{ NodeKey, NodeKeyBase }

class PathSpec extends QuestionsSpec {
  "root returns the last tail" in {
    val last      = qn(NodeKey.random)
    val ancestors = qn(NodeKey.random) :: qn(NodeKey.random) :: AncestorList(last)
    root(ancestors) mustBe AncestorList(last).asRight
  }

  "relative returns what it's given" in {
    val ancestors = qn(NodeKey.random) :: qn(NodeKey.random) :: AncestorList(qn(NodeKey.random))
    relative(ancestors) mustBe ancestors.asRight
  }

  "ancestor" - {
    "returns the node in the ancestor list with the node key base matching the given node key base" in {
      val toMatch          = qn(NodeKey.random)
      val toMatchAncestors = toMatch :: AncestorList(qn(NodeKey.random))
      val ancestors        = qn(NodeKey.random) :: qn(NodeKey.random) :: toMatchAncestors
      (relative \ toMatch.keyBase)(ancestors) mustBe toMatchAncestors.asRight
    }

    "returns an error if the node cannot be found" in {
      val from      = NodeKey.random
      val toFind    = NodeKeyBase.random
      val ancestors = qn(from) :: qn(NodeKey.random) :: AncestorList(qn(NodeKey.random))
      (relative \ toFind)(ancestors) mustBe Left(PathElement.failedToFindAncestor(toFind, from))
    }
  }

  "descendant" - {
    "returns the descendant of the head node in the ancestor list with the node key base matching the given node key base" in {
      val toMatch   = qn(NodeKey.random)
      val ancestors = AncestorList(nonRepeatingParent(NodeKey.random, qn(NodeKey.random), toMatch, qn(NodeKey.random)))
      (relative / toMatch.keyBase)(ancestors) mustBe (toMatch :: ancestors).asRight
    }

    "returns an error if the node cannot be found without recursing through a repeating parent" in {
      val from      = NodeKey.random
      val toFind    = qn(NodeKey.random)
      val ancestors = AncestorList(nonRepeatingParent(from, qn(NodeKey.random), rqn(toFind), qn(NodeKey.random)))
      (relative / toFind.keyBase)(ancestors) mustBe Left(PathElement.failedToFindDescendant(toFind.keyBase, from))
    }
  }
}

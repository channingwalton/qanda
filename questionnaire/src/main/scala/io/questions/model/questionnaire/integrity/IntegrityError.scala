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

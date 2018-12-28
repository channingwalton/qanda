package io.questions.util.specs

import cats.data.NonEmptyList
import io.circe.Json
import io.circe.parser.parse
import io.questions.model.questionnaire.AnswerDeriver.JavaScriptAnswerDeriver
import io.questions.model.questionnaire.Element.{ Derived, NonRepeatingParent, Primitive }
import io.questions.model.questionnaire.nodekey.{ NodeKey, NodeKeyBase, NodeKeyExtension }
import io.questions.model.questionnaire._

trait QuestionnaireUtils {

  implicit def stringToFieldName(s: String): FieldName       = FieldName(s)
  implicit def stringToQuestionText(s: String): QuestionText = QuestionText(s)

  def nonRepeatingParent(key: NodeKey, kid: QuestionnaireNode, kids: QuestionnaireNode*): QuestionnaireNode =
    QuestionnaireNode(key, "", "", NonRepeatingParent(kid, kids: _*))

  def qn(fieldName: String, kid: QuestionnaireNode, kids: QuestionnaireNode*): QuestionnaireNode =
    QuestionnaireNode(nodekey.NodeKey(fieldName), fieldName, fieldName, NonRepeatingParent(kid, kids: _*))

  def rqn(fieldName: String, kid: QuestionnaireNode, kids: QuestionnaireNode*): QuestionnaireNode =
    QuestionnaireNode(nodekey.NodeKey(fieldName), fieldName, fieldName, Element.Parent(repeating = true, NonEmptyList.of(kid, kids: _*)))

  def rqn(kid: QuestionnaireNode, kids: QuestionnaireNode*): QuestionnaireNode =
    QuestionnaireNode(nodeKey, "", "", Element.Parent(repeating = true, NonEmptyList.of(kid, kids: _*)))

  def qn(fieldName: String, ans: String): QuestionnaireNode =
    QuestionnaireNode(nodekey.NodeKey(fieldName), fieldName, fieldName, Primitive(PrimitiveAnswer.StringAnswer(Some(ans))))

  def qn(key: NodeKey, ans: String): QuestionnaireNode =
    QuestionnaireNode(key, "", "", Primitive(PrimitiveAnswer.StringAnswer(Some(ans))))

  def qn(key: NodeKey): QuestionnaireNode =
    QuestionnaireNode(key, "", "", Primitive(PrimitiveAnswer.StringAnswer(None)))

  def qn(keyBase: NodeKeyBase, keyExtension: NodeKeyExtension, ans: String): QuestionnaireNode =
    QuestionnaireNode(NodeKey(keyBase, keyExtension), "", "", Primitive(PrimitiveAnswer.StringAnswer(Some(ans))))

  def qn(key: String, optional: QuestionnaireNodePredicate, validation: QuestionnaireNodePredicate): QuestionnaireNode =
    qn(key, "").copy(optional = optional, validation = Validation.Simple(validation, "foo"))

  def qn(key: NodeKey, optional: QuestionnaireNodePredicate, validation: QuestionnaireNodePredicate): QuestionnaireNode =
    qn(key, "").copy(optional = optional, validation = Validation.Simple(validation, "foo"))

  def intqn(fieldName: String): QuestionnaireNode =
    QuestionnaireNode(nodeKey, fieldName, fieldName, Primitive(PrimitiveAnswer.IntAnswer(None)))

  def intqn(fieldName: String, value: Int): QuestionnaireNode =
    QuestionnaireNode(nodeKey, fieldName, fieldName, Primitive(PrimitiveAnswer.IntAnswer(Some(value))))

  def derivedIntqn(fieldName: String, scriptWithApplyFunction: String): QuestionnaireNode =
    QuestionnaireNode(nodeKey,
                      fieldName,
                      fieldName,
                      Derived(PrimitiveAnswer.IntAnswer(None), JavaScriptAnswerDeriver(scriptWithApplyFunction, "apply")))

  def nodeKey: NodeKey = nodekey.NodeKey.random

  def parseUnsafe(s: String): Json = parse(s.stripMargin).left.map(throw _).merge
}

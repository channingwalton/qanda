package io.questions.testdata

import java.util.UUID

import cats.data.NonEmptyList
import io.questions.model.questionnaire.Element.Primitive
import io.questions.model.questionnaire.Path.root
import io.questions.model.questionnaire.PrimitiveAnswer.{ EnumerationAnswer, StringAnswer }
import io.questions.model.questionnaire._
import io.questions.model.questionnaire.nodekey.NodeKey
import io.questions.testdata.samples.enumerations.YesNo
import io.questions.model.questionnaire.QuestionnaireNodePredicate._
import Components._

object ExampleComponents {

  val isBackOfficeUserKey: NodeKey = NodeKey("d5a26526-02a8-41cc-9030-03cc83d6e3c1")

  def id(uuid: UUID): QuestionnaireNode =
    QuestionnaireNode(
      idKey,
      FieldName("id"),
      QuestionText("ID"),
      Primitive(StringAnswer(Some(uuid.toString)))
    )

  def isBackOfficeUser: QuestionnaireNode =
    QuestionnaireNode(
      isBackOfficeUserKey,
      FieldName("isBackOfficeUser"),
      QuestionText("Is the current user a backoffice user?"),
      Primitive(EnumerationAnswer(None, YesNo.name))
    )

  def metadata(questionnaireId: QuestionnaireId): QuestionnaireNode =
    QuestionnaireNode(
      metadataKey,
      FieldName("metadata"),
      QuestionText("metadata"),
      Element.NonRepeatingParent(id(questionnaireId.id), isBackOfficeUser),
      visibility = QuestionnaireNodePredicate.False
    )

  // scalastyle:off parameter.number
  def standard(key: NodeKey,
               name: FieldName,
               text: QuestionText,
               element: Element.Parent,
               optional: QuestionnaireNodePredicate = QuestionnaireNodePredicate.False,
               validation: Validation = Validation.AlwaysValid,
               visibility: QuestionnaireNodePredicate = QuestionnaireNodePredicate.True,
               editability: QuestionnaireNodePredicate = QuestionnaireNodePredicate.True,
               meta: Seq[NodeMetadata] = Seq.empty,
               enums: Map[EnumerationName, NonEmptyList[EnumerationValue]] = Map.empty,
               questionnaireId: QuestionnaireId = QuestionnaireId.random): QuestionnaireNode =
    QuestionnaireNode(
      key: NodeKey,
      name,
      text,
      element.copy(children = enumerations(enums) :: metadata(questionnaireId) :: element.children),
      optional,
      validation,
      visibility,
      editability,
      meta
    )

  def approvalQuestion(fieldName: FieldName, question: QuestionText): QuestionnaireNode =
    QuestionnaireNode(
      NodeKey.random,
      fieldName,
      question,
      Element.Primitive(PrimitiveAnswer.EnumerationAnswer(None, YesNo.name)),
      visibility = root / ExampleComponents.isBackOfficeUserKey.base === YesNo.yes.key
    )
}

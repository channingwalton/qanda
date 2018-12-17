package io.questions.testdata.samples.samplequestionnaire

import cats.syntax.show._
import io.questions.testdata.ExampleComponents
import io.questions.model.questionnaire.FieldName.FieldNameStringSyntax
import io.questions.model.questionnaire.QuestionText.QuestionTextSyntax
import io.questions.model.questionnaire.integrity.IntegrityError
import io.questions.model.questionnaire.nodekey.NodeKey
import io.questions.model.questionnaire.{ Element, QuestionnaireId, QuestionnaireNode }
import io.questions.testdata.samples.enumerations._
import io.questions.testdata.ExampleComponents

object SampleQuestionnaire {
  val id: QuestionnaireId = QuestionnaireId.random

  val questionnaire: QuestionnaireNode = ExampleComponents.standard(
    NodeKey.random,
    "applicant".fieldName,
    "Applicant".text,
    Element.NonRepeatingParent(
      SampleQuestionsSection.section,
      DependentQuestionsSection.section,
      OtherElementsSection.section
    ),
    enums = Map(
      Country.name            → Country.values,
      Gender.name             → Gender.values,
      RelationshipStatus.name → RelationshipStatus.values,
      Title.name              → Title.values,
      YesNo.name              → YesNo.values,
      Currencies.name         → Currencies.values
    ),
    questionnaireId = id
  )

  private val integrity: Set[IntegrityError] = questionnaire.checkIntegrity
  if (integrity.nonEmpty) println(s"Integrity errors found in the SampleQuestionnaire:\n${integrity.map(_.show).mkString("\n")}") //scalastyle:ignore
}

package io.questions.testdata.samples.questions

import io.questions.model.questionnaire.FieldName.FieldNameStringSyntax
import io.questions.model.questionnaire.QuestionText.QuestionTextSyntax
import io.questions.model.questionnaire._
import io.questions.model.questionnaire.QuestionnaireNodePredicate._
import io.questions.testdata.samples.samplequestionnaire.QuestionTypes.{ stringQuestion, titleQuestion }

object PersonalQuestions {
  def title: QuestionnaireNode = titleQuestion("title".fieldName, "Title".text)
  def firstName: QuestionnaireNode =
    stringQuestion("firstName".fieldName, "First name".text)
      .validateSelf(_.isAlphabetic, "Only letters are allowed")

  def middleNames: QuestionnaireNode =
    stringQuestion("middleNames".fieldName, "Middle names".text).makeOptional.validateSelf(_.isAlphabetic, "Only letters are allowed")
  def lastName: QuestionnaireNode =
    stringQuestion("lastName".fieldName, "Last name".text).validateSelf(_.isAlphabetic, "Only letters are allowed")

  def fullName(fieldName: FieldName, label: String): QuestionnaireNode =
    nonRepeatingParentQuestion(fieldName, label.text, title, firstName, middleNames, lastName)

  val currentFirstName: QuestionnaireNode = firstName
  val currentLastName: QuestionnaireNode  = lastName
  val currentFullName: QuestionnaireNode =
    nonRepeatingParentQuestion("currentFullName".fieldName, "Name".text, title, currentFirstName, middleNames, currentLastName)
}

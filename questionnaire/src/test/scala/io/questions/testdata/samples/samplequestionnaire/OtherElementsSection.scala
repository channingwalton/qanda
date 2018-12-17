package io.questions.testdata.samples.samplequestionnaire

import io.questions.testdata.samples.questions.PersonalQuestions._
import io.questions.model.questionnaire.FieldName._
import io.questions.model.questionnaire.QuestionText._
import io.questions.model.questionnaire._
import io.questions.testdata.samples.samplequestionnaire.QuestionTypes._

object OtherElementsSection {

  private val derivedPrimAnswer = PrimitiveAnswer.StringAnswer()
  private val deriver = AnswerDeriver.JavaScriptAnswerDeriver(
    """|var calculateRisk = function(answers, k, ke) {
       |  if (answers.applicant.sampleQuestions.enumerationsQuestionsPage.single.indexOf('Iraq') != -1)
       |    return stringResult('High')
       |  else
       |    return stringResult('Low')
       |}""".stripMargin,
    "calculateRisk"
  )

  val groupedQuestionsPage: QuestionnaireNode = pageQuestion(
    "groupedQuestionsPage".fieldName,
    "Grouped Questions".text,
    fullName("single".fieldName, "Single"),
    fullName("optional".fieldName, "Optional").makeOptional,
    fullName("readOnly".fieldName, "Read Only").readOnly,
    fullName("invisible".fieldName, "Invisible").hide,
    fullName("error".fieldName, "Error").validate("Validation Error in this question", QuestionnaireNodePredicate.False),
    nonRepeatingParentQuestion("nonRepeatingParent".fieldName,
                               "Non Repeating (Parent)".text,
                               fullName("nonRepeating".fieldName, "Non Repeating (Children)")),
    repeatingParentQuestion("repeatingParent".fieldName, "Repeating (Parent)".text, fullName("repeating".fieldName, "Repeating (Children)"))
  )

  val infoLabelsPage: QuestionnaireNode = pageQuestion(
    "infoLabelsPage".fieldName,
    "Info Labels".text,
    info("single".fieldName, "Single".text, "This is a sample of a single info element"),
    info("optional".fieldName, "Optional".text, "This is a sample of an info element tagged as optional").makeOptional,
    info("disabled".fieldName, "Disabled".text, "This is a sample of an info element tagged as disabled").readOnly,
    info("invisible".fieldName, "Invisible".text, "This is a sample of an info element tagged as non visible").hide
  )

  val derivedQuestionsPage: QuestionnaireNode = pageQuestion(
    "derivedQuestions".fieldName,
    "Derived Questions".text,
    derived("single".fieldName, "Derived".text, derivedPrimAnswer, deriver),
    derived("optional".fieldName, "Optional".text, derivedPrimAnswer, deriver).makeOptional,
    derived("disabled".fieldName, "Disabled".text, derivedPrimAnswer, deriver).readOnly,
    derived("invisible".fieldName, "Invisible".text, derivedPrimAnswer, deriver).hide,
    derived("error".fieldName, "Error".text, derivedPrimAnswer, deriver)
      .validate("Validation Error in this question", QuestionnaireNodePredicate.False),
    nonRepeatingParentQuestion(
      "nonRepeatingParent".fieldName,
      "Non Repeating (Parent)".text,
      derived("nonRepeating".fieldName, "Non Repeating (Children)".text, derivedPrimAnswer, deriver)
    ),
    repeatingParentQuestion("repeatingParent".fieldName,
                            "Repeating (Parent)".text,
                            derived("repeating".fieldName, "Repeating (Children)".text, derivedPrimAnswer, deriver))
  )

  val section: QuestionnaireNode = sectionQuestion(
    "otherElements".fieldName,
    "Other Elements".text,
    groupedQuestionsPage,
    infoLabelsPage,
    //TODO: derived questions don't work on questionnaire load, need to check why but lack proper understanding of the derivation process
//    derivedQuestionsPage
  )
}

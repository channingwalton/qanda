package io.questions.testdata.samples.samplequestionnaire

import eu.timepit.refined.auto._
import io.questions.model.questionnaire._
import io.questions.model.questionnaire.FieldName._
import io.questions.model.questionnaire.QuestionText._
import io.questions.testdata.samples.samplequestionnaire.QuestionTypes._

object SampleQuestionsSection {

  val stringQuestionsPage: QuestionnaireNode = pageQuestion(
    "stringQuestionsPage".fieldName,
    "String Questions".text,
    stringQuestion("single".fieldName, "Single".text),
    stringQuestion("optional".fieldName, "Optional".text).makeOptional,
    stringQuestion("readOnly".fieldName, "Read Only".text).readOnly,
    stringQuestion("invisible".fieldName, "Invisible".text).hide,
    stringQuestion("error".fieldName, "Error".text).validate("Validation Error in this question", QuestionnaireNodePredicate.False),
    nonRepeatingParentQuestion("nonRepeatingParent".fieldName,
                               "Non Repeating (Parent)".text,
                               stringQuestion("nonRepeatingString".fieldName, "Non Repeating (Children)".text)),
    repeatingParentQuestion("repeatingParent".fieldName,
                            "Repeating (Parent)".text,
                            stringQuestion("repeatingString".fieldName, "Repeating (Children)".text))
  )

  val intQuestionsPage: QuestionnaireNode = pageQuestion(
    "intQuestionsPage".fieldName,
    "Numeric Questions".text,
    numericQuestion("single".fieldName, "Single".text),
    numericQuestion("optional".fieldName, "Optional".text).makeOptional,
    numericQuestion("readOnly".fieldName, "Read Only".text).readOnly,
    numericQuestion("invisible".fieldName, "Invisible".text).hide,
    numericQuestion("error".fieldName, "Error".text).validate("Validation Error in this question", QuestionnaireNodePredicate.False),
    nonRepeatingParentQuestion("nonRepeatingParent".fieldName,
                               "Non Repeating (Parent)".text,
                               numericQuestion("nonRepeating".fieldName, "Non Repeating (Children)".text)),
    repeatingParentQuestion("repeatingParent".fieldName,
                            "Repeating (Parent)".text,
                            numericQuestion("repeating".fieldName, "Repeating (Children)".text))
  )

  val bigDecimalQuestionsPage: QuestionnaireNode = pageQuestion(
    "bigDecimalQuestionsPage".fieldName,
    "Big Decimal Questions".text,
    bigDecimalQuestion("single".fieldName, "Single".text),
    bigDecimalQuestion("optional".fieldName, "Optional".text).makeOptional,
    bigDecimalQuestion("readOnly".fieldName, "Read Only".text).readOnly,
    bigDecimalQuestion("invisible".fieldName, "Invisible".text).hide,
    bigDecimalQuestion("error".fieldName, "Error".text).validate("Validation Error in this question", QuestionnaireNodePredicate.False),
    nonRepeatingParentQuestion("nonRepeatingParent".fieldName,
                               "Non Repeating (Parent)".text,
                               bigDecimalQuestion("nonRepeating".fieldName, "Non Repeating (Children)".text)),
    repeatingParentQuestion("repeatingParent".fieldName,
                            "Repeating (Parent)".text,
                            bigDecimalQuestion("repeating".fieldName, "Repeating (Children)".text))
  )

  val enumerationsQuestionsPage: QuestionnaireNode = pageQuestion(
    "enumerationsQuestionsPage".fieldName,
    "Enumeration Questions".text,
    countryQuestion("single".fieldName, "Single".text),
    countryQuestion("optional".fieldName, "Optional".text).makeOptional,
    countryQuestion("readOnly".fieldName, "Read Only".text).readOnly,
    countryQuestion("invisible".fieldName, "Invisible".text).hide,
    countryQuestion("error".fieldName, "Error".text).validate("Validation Error in this question", QuestionnaireNodePredicate.False),
    nonRepeatingParentQuestion("nonRepeatingParent".fieldName,
                               "Non Repeating (Parent)".text,
                               countryQuestion("nonRepeating".fieldName, "Non Repeating (Children)".text)),
    repeatingParentQuestion("repeatingParent".fieldName,
                            "Repeating (Parent)".text,
                            countryQuestion("repeating".fieldName, "Repeating (Children)".text))
  )

  val otherEnumerationsQuestionsPage: QuestionnaireNode = pageQuestion(
    "otherEnumerationsQuestionsPage".fieldName,
    "More Enumerations".text,
    yesNoQuestion("single".fieldName, "Single".text),
    titleQuestion("optional".fieldName, "Optional".text).makeOptional,
    genderQuestion("readOnly".fieldName, "Read Only".text).readOnly,
    yesNoQuestion("invisible".fieldName, "Invisible".text).hide,
    genderQuestion("error".fieldName, "Error".text).validate("Validation Error in this question", QuestionnaireNodePredicate.False),
    nonRepeatingParentQuestion("nonRepeatingParent".fieldName,
                               "Non Repeating (Parent)".text,
                               titleQuestion("nonRepeating".fieldName, "Non Repeating (Children)".text)),
    repeatingParentQuestion("repeatingParent".fieldName,
                            "Repeating (Parent)".text,
                            titleQuestion("repeating".fieldName, "Repeating (Children)".text))
  )

  val moneyQuestions: QuestionnaireNode = pageQuestion(
    "moneyQuestions".fieldName,
    "Money Questions".text,
    moneyQuestion("single".fieldName, "Single".text),
    moneyQuestion("optional".fieldName, "Optional".text).makeOptional,
    moneyQuestion("readOnly".fieldName, "Read Only".text).readOnly,
    moneyQuestion("invisible".fieldName, "Invisible".text).hide,
    moneyQuestion("error".fieldName, "Error".text).validate("Validation Error in this question", QuestionnaireNodePredicate.False),
    nonRepeatingParentQuestion("nonRepeatingParent".fieldName,
                               "Non Repeating (Parent)".text,
                               moneyQuestion("nonRepeating".fieldName, "Non Repeating (Children)".text)),
    repeatingParentQuestion("repeatingParent".fieldName,
                            "Repeating (Parent)".text,
                            moneyQuestion("repeating".fieldName, "Repeating (Children)".text))
  )

  val dateQuestionsPage: QuestionnaireNode = pageQuestion(
    "dateQuestionsPage".fieldName,
    "Date Questions".text,
    dateQuestion("single".fieldName, "Single".text),
    dateQuestion("optional".fieldName, "Optional".text).makeOptional,
    dateQuestion("readOnly".fieldName, "Read Only".text).readOnly,
    dateQuestion("invisible".fieldName, "Invisible".text).hide,
    dateQuestion("error".fieldName, "Error".text).validate("Validation Error in this question", QuestionnaireNodePredicate.False),
    nonRepeatingParentQuestion("nonRepeatingParent".fieldName,
                               "Non Repeating (Parent)".text,
                               dateQuestion("nonRepeating".fieldName, "Non Repeating (Children)".text)),
    repeatingParentQuestion("repeatingParent".fieldName,
                            "Repeating (Parent)".text,
                            dateQuestion("repeating".fieldName, "Repeating (Children)".text))
  )

  val dateTimeQuestionsPage: QuestionnaireNode = pageQuestion(
    "dateTimeQuestionsPage".fieldName,
    "DateTime Questions".text,
    dateTimeQuestion("single".fieldName, "Single".text),
    dateTimeQuestion("optional".fieldName, "Optional".text).makeOptional,
    dateTimeQuestion("readOnly".fieldName, "Read Only".text).readOnly,
    dateTimeQuestion("invisible".fieldName, "Invisible".text).hide,
    dateTimeQuestion("error".fieldName, "Error".text).validate("Validation Error in this question", QuestionnaireNodePredicate.False),
    nonRepeatingParentQuestion("nonRepeatingParent".fieldName,
                               "Non Repeating (Parent)".text,
                               dateTimeQuestion("nonRepeating".fieldName, "Non Repeating (Children)".text)),
    repeatingParentQuestion("repeatingParent".fieldName,
                            "Repeating (Parent)".text,
                            dateTimeQuestion("repeating".fieldName, "Repeating (Children)".text))
  )

  val dateTimeWithZoneQuestionsPage: QuestionnaireNode = pageQuestion(
    "dateTimeWithZoneQuestionsPage".fieldName,
    "DateTimeZone Questions".text,
    dateTimeWithTimeZoneQuestion("single".fieldName, "Single".text),
    dateTimeWithTimeZoneQuestion("optional".fieldName, "Optional".text).makeOptional,
    dateTimeWithTimeZoneQuestion("readOnly".fieldName, "Read Only".text).readOnly,
    dateTimeWithTimeZoneQuestion("invisible".fieldName, "Invisible".text).hide,
    dateTimeWithTimeZoneQuestion("error".fieldName, "Error".text)
      .validate("Validation Error in this question", QuestionnaireNodePredicate.False),
    nonRepeatingParentQuestion("nonRepeatingParent".fieldName,
                               "Non Repeating (Parent)".text,
                               dateTimeWithTimeZoneQuestion("nonRepeating".fieldName, "Non Repeating (Children)".text)),
    repeatingParentQuestion("repeatingParent".fieldName,
                            "Repeating (Parent)".text,
                            dateTimeWithTimeZoneQuestion("repeating".fieldName, "Repeating (Children)".text))
  )

  val timeQuestionsPage: QuestionnaireNode = pageQuestion(
    "timeQuestionsPage".fieldName,
    "Time Questions".text,
    timeQuestion("single".fieldName, "Single".text),
    timeQuestion("optional".fieldName, "Optional".text).makeOptional,
    timeQuestion("readOnly".fieldName, "Read Only".text).readOnly,
    timeQuestion("invisible".fieldName, "Invisible".text).hide,
    timeQuestion("error".fieldName, "Error".text).validate("Validation Error in this question", QuestionnaireNodePredicate.False),
    nonRepeatingParentQuestion("nonRepeatingParent".fieldName,
                               "Non Repeating (Parent)".text,
                               timeQuestion("nonRepeating".fieldName, "Non Repeating (Children)".text)),
    repeatingParentQuestion("repeatingParent".fieldName,
                            "Repeating (Parent)".text,
                            timeQuestion("repeating".fieldName, "Repeating (Children)".text))
  )

  val timeWithZoneQuestionsPage: QuestionnaireNode = pageQuestion(
    "timeWithZoneQuestionsPage".fieldName,
    "TimeZone Questions".text,
    timeWithTimeZoneQuestion("single".fieldName, "Single".text),
    timeWithTimeZoneQuestion("optional".fieldName, "Optional".text).makeOptional,
    timeWithTimeZoneQuestion("readOnly".fieldName, "Read Only".text).readOnly,
    timeWithTimeZoneQuestion("invisible".fieldName, "Invisible".text).hide,
    timeWithTimeZoneQuestion("error".fieldName, "Error".text)
      .validate("Validation Error in this question", QuestionnaireNodePredicate.False),
    nonRepeatingParentQuestion("nonRepeatingParent".fieldName,
                               "Non Repeating (Parent)".text,
                               timeWithTimeZoneQuestion("nonRepeating".fieldName, "Non Repeating (Children)".text)),
    repeatingParentQuestion("repeatingParent".fieldName,
                            "Repeating (Parent)".text,
                            timeWithTimeZoneQuestion("repeating".fieldName, "Repeating (Children)".text))
  )

  val multipleQuestionsPage: QuestionnaireNode = pageQuestion(
    "multipleQuestionsPage".fieldName,
    "Multiple Questions".text,
    stringQuestion("singleString".fieldName, "Single String".text),
    numericQuestion("singleNumeric".fieldName, "Single Numeric".text),
    bigDecimalQuestion("singleBigDecimal".fieldName, "Single BigDecimal".text),
    countryQuestion("singleEnumeration".fieldName, "Single Enumeration".text),
    moneyQuestion("singleMoney".fieldName, "Single Money".text),
    dateQuestion("singleDate".fieldName, "SingleDate".text),
    dateTimeQuestion("singleDateTime".fieldName, "Single DateTime".text),
    dateTimeWithTimeZoneQuestion("singleDateTimeWithTZ".fieldName, "Single DateTime with Timezone".text),
    timeQuestion("singleTime".fieldName, "Single Time".text),
    timeWithTimeZoneQuestion("singleTimeWithZone".fieldName, "Time with zone".text),
  )

  val section: QuestionnaireNode = sectionQuestion(
    "sampleQuestions".fieldName,
    "Sample Questions".text,
    stringQuestionsPage,
    intQuestionsPage,
    bigDecimalQuestionsPage,
    enumerationsQuestionsPage,
    otherEnumerationsQuestionsPage,
    moneyQuestions,
    dateQuestionsPage,
    dateTimeQuestionsPage,
    dateTimeWithZoneQuestionsPage,
    timeQuestionsPage,
    timeWithZoneQuestionsPage,
    multipleQuestionsPage
  )
}

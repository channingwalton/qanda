package io.questions.derivedvalues

import io.questions.QuestionsSpec
import io.questions.derivedvalues.DerivedValuesResult.{ InsufficientData, Result }
import io.questions.model.questionnaire.PrimitiveAnswer.{ IntAnswer, StringAnswer }

class ScriptRunnerSpec extends QuestionsSpec {
  val intQuestion = intqn("abc", 1)

  "insufficientData" in {
    ScriptRunner(
      """|var myScript = function(a, k, ke) {
         |  return insufficientData('Boo!')
         |}""".stripMargin,
      "myScript",
      intQuestion,
      intQuestion.key
    ) mustBe InsufficientData("Boo!")
  }

  "intResult" in {
    ScriptRunner(
      """|var myScript = function(a, k, ke) {
         |  return intResult(2)
         |}""".stripMargin,
      "myScript",
      intQuestion,
      intQuestion.key
    ) mustBe Result(IntAnswer(Some(2)))
  }

  "stringResult" in {
    ScriptRunner(
      """|var myScript = function(a, k, ke) {
         |  return stringResult('x')
         |}""".stripMargin,
      "myScript",
      intQuestion,
      intQuestion.key
    ) mustBe Result(StringAnswer(Some("x")))
  }

  "Hello, World" in {
    val q = qn("root", qn("first", "Hello"), qn("second", "World"))

    ScriptRunner(
      """|var myScript = function(a, k, ke) {
         |  return stringResult(a.root.first + ', ' + a.root.second)
         |}""".stripMargin,
      "myScript",
      q,
      q.key
    ) mustBe Result(StringAnswer(Some("Hello, World")))
  }
}

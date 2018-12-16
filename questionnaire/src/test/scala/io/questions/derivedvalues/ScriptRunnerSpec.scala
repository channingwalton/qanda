/*
 * Copyright 2018 TBD
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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

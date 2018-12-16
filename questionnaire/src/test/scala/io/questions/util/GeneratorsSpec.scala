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

package io.questions.util

import io.questions.QuestionsSpec
import io.questions.model.questionnaire.PrimitiveAnswer
import org.scalacheck.Gen

// Even generators need tests sometimes
class GeneratorsSpec extends QuestionsSpec {

  "primitiveAnswerGen" - {
    val allPrimitiveAnswerTypes: Set[String] = adtMembers[PrimitiveAnswer]()
    "all generated entities are known PrimitiveAnswers" in {
      forAll(Gen.listOfN(100, primitiveAnswerGen)) { list ⇒
        val listAsNames = list.map(_.getClass.getSimpleName)
        listAsNames.forall(allPrimitiveAnswerTypes.contains) mustBe true
      }
    }
    "generates entities for all known PrimitiveAnswers" in {
      forAll(Gen.listOfN(100, primitiveAnswerGen)) { list ⇒
        val listAsNames    = list.map(_.getClass.getSimpleName)
        val remainingTypes = allPrimitiveAnswerTypes.dropWhile(listAsNames.contains)
        remainingTypes mustBe Set.empty
      }
    }
  }

}

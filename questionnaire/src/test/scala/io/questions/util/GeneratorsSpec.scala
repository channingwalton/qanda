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

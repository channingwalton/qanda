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

package io.questions.model.questionnaire

import java.time._

import io.questions.QuestionsSpec
import io.questions.model.questionnaire.PrimitiveAnswer.{ DateTimeAnswer, EnumerationAnswer, StringAnswer }
import org.scalacheck.Gen

class PrimitiveAnswerSpec extends QuestionsSpec {

  "valueEquals" - {
    "answers equal as expected" in {
      forAll(primitiveAnswerGen, primitiveAnswerGen) { (a1, a2) ⇒
        whenever(a1.getClass !== a2.getClass) {
          PrimitiveAnswer.valueEquals(a1, a1).right.value mustBe true
          PrimitiveAnswer.valueEquals(a2, a2).right.value mustBe true

          // we must consider a special case used for predicate construction
          (a1, a2) match {
            case (s1: StringAnswer, s2: EnumerationAnswer) ⇒
              PrimitiveAnswer.valueEquals(a1, a2).right.value mustBe (s1.answer === s2.answer)
            case (s1: EnumerationAnswer, s2: StringAnswer) ⇒
              PrimitiveAnswer.valueEquals(a1, a2).right.value mustBe (s1.answer === s2.answer)
            case _ ⇒
              PrimitiveAnswer.valueEquals(a1, a2).left.value mustBe s"Mismatched types in valueEquals: ${a1.getClass} && ${a2.getClass}"
          }
        }
      }
    }
    "string and enumeration answers can be compared via valueEquals to facilitate predicate construction" in {
      forAll(stringAnswerGen, enumerationAnswerGen) { (a1, a2) ⇒
        PrimitiveAnswer.valueEquals(a1, a2).right.value mustBe (a1.answer === a2.answer)
        PrimitiveAnswer.valueEquals(a2, a1).right.value mustBe (a2.answer === a1.answer)
      }
    }
    "DateTime answers" - {
      "Two date time answers with different values are not equal" in {
        forAll(dateTimeAnswerGen, dateTimeAnswerGen) { (a1, a2) ⇒
          PrimitiveAnswer.valueEquals(a1, a2).right.value mustBe (a1 === a2)
        }
      }
    }
  }

  "typeCheck" - {
    "answers typecheck as expected" in {
      forAll(primitiveAnswerGen, primitiveAnswerGen) { (a1, a2) ⇒
        whenever(a1.getClass !== a2.getClass) {
          PrimitiveAnswer.typeCheck(a1, a1).right.value mustBe true
          PrimitiveAnswer.typeCheck(a2, a2).right.value mustBe true

          // we must consider a special case used for predicate construction
          (a1, a2) match {
            case (_: StringAnswer, _: EnumerationAnswer) ⇒ PrimitiveAnswer.typeCheck(a1, a2).right.value mustBe true
            case (_: EnumerationAnswer, _: StringAnswer) ⇒ PrimitiveAnswer.typeCheck(a1, a2).right.value mustBe true
            case _                                       ⇒ PrimitiveAnswer.typeCheck(a1, a2).left.value mustBe s"Mismatched types in typeCheck: ${a1.getClass} && ${a2.getClass}"
          }
        }
      }
    }
    "string and enumeration answers typecheck to facilitate predicate construction" in {
      forAll(stringAnswerGen, enumerationAnswerGen) { (a1, a2) ⇒
        PrimitiveAnswer.typeCheck(a1, a2).right.value mustBe true
        PrimitiveAnswer.typeCheck(a2, a1).right.value mustBe true
      }
    }
  }

  "DateTimeAnswer" - {
    "asLocalDate" - {
      "is left if we have no local date" in {
        forAll(Gen.option(localTimeGen), Gen.option(zoneTimeGen)) { (time, zone) ⇒
          val answer = DateTimeAnswer(None, time, zone)
          answer.asLocalDate.left.value mustBe "No LocalDate available"
        }
      }
      "is right if we have a local date, and returns the local date" in {
        forAll(localDateGen, Gen.option(localTimeGen), Gen.option(zoneTimeGen)) { (date, time, zone) ⇒
          val answer = DateTimeAnswer(Some(date), time, zone)
          answer.asLocalDate.right.value mustBe date
        }
      }
    }
    "asLocalTime" - {
      "is left if we have no local time" in {
        forAll(Gen.option(localDateGen), Gen.option(zoneTimeGen)) { (date, zone) ⇒
          val answer = DateTimeAnswer(date, None, zone)
          answer.asLocalTime.left.value mustBe "No LocalTime available"
        }
      }
      "is right if we have a local time, and returns the local time" in {
        forAll(Gen.option(localDateGen), localTimeGen, Gen.option(zoneTimeGen)) { (date, time, zone) ⇒
          val answer = DateTimeAnswer(date, Some(time), zone)
          answer.asLocalTime.right.value mustBe time
        }
      }
    }
    "asLocalDateTime" - {
      "is left if we have no local date" in {
        forAll(Gen.option(localTimeGen), Gen.option(zoneTimeGen)) { (time, zone) ⇒
          val answer = DateTimeAnswer(None, time, zone)
          answer.asLocalDateTime.left.value mustBe "Can't construct a LocalDateTime without a LocalDate or a LocalTime"
        }
      }
      "is left if we have no local time" in {
        forAll(Gen.option(localDateGen), Gen.option(zoneTimeGen)) { (date, zone) ⇒
          val answer = DateTimeAnswer(date, None, zone)
          answer.asLocalDateTime.left.value mustBe "Can't construct a LocalDateTime without a LocalDate or a LocalTime"
        }
      }
      "is right if we have a local date an time, and returns the local date time" in {
        forAll(localDateGen, localTimeGen, Gen.option(zoneTimeGen)) { (date, time, zone) ⇒
          val answer = DateTimeAnswer(Some(date), Some(time), zone)
          answer.asLocalDateTime.right.value mustBe LocalDateTime.of(date, time)
        }
      }
    }
    "asOffsetTime" - {
      "is left if we have no local date" in {
        forAll(Gen.option(localTimeGen), Gen.option(zoneTimeGen)) { (time, zone) ⇒
          val answer = DateTimeAnswer(None, time, zone)
          answer.asOffsetTime.left.value mustBe "Can't construct an OffsetTime without a LocalDate, LocalTime, or a ZoneId"
        }
      }
      "is left if we have no local time" in {
        forAll(Gen.option(localDateGen), Gen.option(zoneTimeGen)) { (date, zone) ⇒
          val answer = DateTimeAnswer(date, None, zone)
          answer.asOffsetTime.left.value mustBe "Can't construct an OffsetTime without a LocalDate, LocalTime, or a ZoneId"
        }
      }
      "is left if we have no zone id" in {
        forAll(Gen.option(localDateGen), Gen.option(localTimeGen)) { (date, time) ⇒
          val answer = DateTimeAnswer(date, time, None)
          answer.asOffsetTime.left.value mustBe "Can't construct an OffsetTime without a LocalDate, LocalTime, or a ZoneId"
        }
      }
      "is right if we have a local time an zone id, and returns the offset time" in {
        forAll(localDateGen, localTimeGen, zoneTimeGen) { (date, time, zone) ⇒
          val answer = DateTimeAnswer(Some(date), Some(time), Some(zone))
          answer.asOffsetTime.right.value mustBe OffsetTime.of(time, zone.getRules.getOffset(LocalDateTime.of(date, time)))
        }
      }
    }
    "asZonedDateTime" - {
      "is left if we have no local date" in {
        forAll(Gen.option(localTimeGen), Gen.option(zoneTimeGen)) { (time, zone) ⇒
          val answer = DateTimeAnswer(None, time, zone)
          answer.asZonedDateTime.left.value mustBe "Can't construct a ZonedDateTime without a LocalDate, LocalTime, or a ZoneId"
        }
      }
      "is left if we have no local time" in {
        forAll(Gen.option(localDateGen), Gen.option(zoneTimeGen)) { (date, zone) ⇒
          val answer = DateTimeAnswer(date, None, zone)
          answer.asZonedDateTime.left.value mustBe "Can't construct a ZonedDateTime without a LocalDate, LocalTime, or a ZoneId"
        }
      }
      "is left if we have no zone id" in {
        forAll(Gen.option(localDateGen), Gen.option(localTimeGen)) { (date, time) ⇒
          val answer = DateTimeAnswer(date, time, None)
          answer.asZonedDateTime.left.value mustBe "Can't construct a ZonedDateTime without a LocalDate, LocalTime, or a ZoneId"
        }
      }
      "is right if we have all required data, and returns the ZonedDateTime" in {
        forAll(localDateGen, localTimeGen, zoneTimeGen) { (date, time, zone) ⇒
          val answer = DateTimeAnswer(Some(date), Some(time), Some(zone))
          answer.asZonedDateTime.right.value mustBe ZonedDateTime.of(date, time, zone.getRules.getOffset(LocalDateTime.of(date, time)))
        }
      }
    }
  }

}

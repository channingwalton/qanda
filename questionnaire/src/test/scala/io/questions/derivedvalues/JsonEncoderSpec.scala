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

import cats.syntax.show._
import io.questions.QuestionsSpec
import io.questions.model.questionnaire.PathUtils
import io.questions.model.questionnaire.PrimitiveAnswer.IntAnswer
import io.questions.model.questionnaire.nodekey.NodeKeyExtension

class JsonEncoderSpec extends QuestionsSpec {
  "Single node" - {
    "Primitive answer" - {
      "Unanswered" in {
        JsonEncoder(intqn("abc")) mustBe parseUnsafe("{}")
      }

      "Answered" in {
        JsonEncoder(intqn("abc", 1)) mustBe parseUnsafe("""{"abc" : 1}""")
      }
    }
  }

  "Node with children" - {
    "Non-Repeating" in {
      JsonEncoder(qn("parent", intqn("a", 1), intqn("b", 2))) mustBe parseUnsafe("""|{
           |  "parent" : {
           |    "a" : 1,
           |    "b" : 2
           |  }
           |}
        """)
    }

    "Repeating" in {
      val e = qn("thing", intqn("a"), intqn("b"))
      val q = rqn("things", e)
      val q6 =
        for {
          q2 ← q.appendChild(q.key, NodeKeyExtension.random)
          q3 <- q2.answerByPath(_ / 0 / "a", IntAnswer(Some(1)))
          q4 <- q3.answerByPath(_ / 0 / "b", IntAnswer(Some(2)))
          q5 <- q4.answerByPath(_ / 1 / "a", IntAnswer(Some(3)))
        } yield q5

      val q7 = q6.right.value
      JsonEncoder(q7) mustBe parseUnsafe(
        s"""|{
           |  "things" : [
           |    {
           |      "keyExtension" : "${(q7 / 0).key.extension.show}",
           |      "a" : 1,
           |      "b" : 2
           |    },
           |    {
           |      "keyExtension" : "${(q7 / 1).key.extension.show}",
           |      "a" : 3
           |    }
           |  ]
           |}
        """
      )
    }
  }
}

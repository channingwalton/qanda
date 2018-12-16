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

package io.questions.model.questionnaire.completion

import cats.{ Monoid, Show }

case class Completion(questions: Int, answers: Int) {
  def isComplete: Boolean = questions == answers
}

object Completion {
  val zero = Completion(0, 0)

  implicit val show: Show[Completion] = new Show[Completion] {
    override def show(t: Completion): String = s"${t.answers}/${t.questions}"
  }

  implicit val monoid: Monoid[Completion] = new Monoid[Completion] {
    override def empty: Completion = zero

    override def combine(x: Completion, y: Completion): Completion = Completion(x.questions + y.questions, x.answers + y.answers)
  }
}

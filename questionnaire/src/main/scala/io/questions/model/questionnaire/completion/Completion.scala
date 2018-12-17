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

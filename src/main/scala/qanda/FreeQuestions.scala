package qanda

import scalaz.{Free, ~>, Id, Coyoneda}

import scalaz.syntax.traverse._

// see http://underscore.io/blog/posts/2015/04/14/free-monads-are-simple.html
object FreeQuestions {

  // The Model
  sealed trait Question[T] {
    def question: String
  }

  case class Answer[T](q: Question[T], a: T)

  // Some Question types
  case class TextQuestion(question: String) extends Question[String]
  case class BooleanQuestion(question: String) extends Question[Boolean]

  type Answerable[A] = Coyoneda[Answer, A]

  def answer[A](question: Question[A], a: A): Free[Answerable, A] = Free.liftFC(Answer(question, a))

  object ToyInterpreter extends (Answer ~> Id.Id) {
    import Id._

    def apply[A](in: Answer[A]): Id[A] =
      in match {
        case Answer(q: TextQuestion, a) ⇒ println(a); a
        case Answer(q: BooleanQuestion, a) ⇒ println(a); a
      }
  }
}

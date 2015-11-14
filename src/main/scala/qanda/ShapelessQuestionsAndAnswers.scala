package qanda

import shapeless._
import shapeless.ops.hlist.Mapper

// The Model
trait ShapelessQuestionsAndAnswersModel {

  sealed trait Question[T] {
    def question: String
  }

  case class Answer[T](q: Question[T], a: T)

  // Some Question types
  case class TextQuestion(question: String) extends Question[String]

  case class BooleanQuestion(question: String) extends Question[Boolean]

  // A function that can map questions to something else - could be Rx elements
  object RxThings extends Poly1 {
    implicit def caseText = at[TextQuestion]((x: TextQuestion) => "Text")

    implicit def caseBoolean = at[BooleanQuestion]((x: BooleanQuestion) => "Boolean")

    // cheeky putting this here but it makes it easier to use
    def apply[L <: HList](list: L)(implicit m: Mapper[RxThings.type, L]): m.Out = list.map(RxThings)
  }

  // A function that can map questions to something else - could be Rx elements
  trait Store[T] {
    def store(v: Answer[T]): Unit
  }

  case object TextStore extends Store[String] {
    def store(v: Answer[String]): Unit = {}
  }

  case object BooleanStore extends Store[Boolean] {
    def store(v: Answer[Boolean]): Unit = {}
  }

  object StoreThings extends Poly1 {
    implicit def caseText = at[Answer[String]]((x: Answer[String]) => TextStore.store(x))

    implicit def caseBoolean = at[Answer[Boolean]]((x: Answer[Boolean]) => BooleanStore.store(x))
  }

  // Put some questions somewhere
  object TheQuestions {
    val bQuestion: BooleanQuestion = BooleanQuestion("Yes or no?")
    val tQuestion: TextQuestion = TextQuestion("What's your answer?")

    val questions = bQuestion :: tQuestion :: HNil
  }

}

// Now map the questions using the RxThings function
object ShapelessQuestionsAndAnswers extends ShapelessQuestionsAndAnswersModel with App {
  println(RxThings(TheQuestions.questions))

  // Now selecting questions of a particular type
  val justText = TheQuestions.questions.filter[TextQuestion]

  // Storing answers
  StoreThings(Answer(TheQuestions.tQuestion, "my answer"))
  StoreThings(Answer(TheQuestions.bQuestion, true))
}
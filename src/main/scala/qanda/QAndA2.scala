package qanda

object QAndA2 {

  // Here is a model of questions that know their type of answer
  // Many question types could have the same answer type, eg StringQuestion and NameQuestion
  object Model {

    sealed trait Question {
      type Ans

      def question: String

      def answer: Option[Ans]

      def run[F[_]](F: QuestionAlgebra[F]): F[Ans]
    }

    trait QuestionAlgebra[F[_]] {
      def textQuestion(): F[String]
      def nameQuestion(): F[String]
      def booleanQuestion(): F[Boolean]
    }

    case class TextQuestion(question: String, answer: Option[String]) extends Question {
      type Ans = String

      def run[F[_]](F: QuestionAlgebra[F]): F[String] = F.textQuestion()
    }

    // Another type of question but same answer type as TextQuestion
    case class NameQuestion(question: String, answer: Option[String]) extends Question {
      type Ans = String

      def run[F[_]](F: QuestionAlgebra[F]): F[String] = F.nameQuestion()
    }

    case class BooleanQuestion(question: String, answer: Option[Boolean]) extends Question {
      type Ans = Boolean

      def run[F[_]](F: QuestionAlgebra[F]): F[Boolean] = F.booleanQuestion()
    }
  }

  // Stores for each type of answer - not question
  object Stores {
    import Model._

    trait AnswerStore[T] { def store(v: Question { type Ans = T }): Unit }

    implicit object StringStore extends AnswerStore[String] {
      def store(v: Question { type Ans = String }): Unit = ???
    }

    implicit object BooleanStore extends AnswerStore[Boolean] {
      def store(v: Question { type Ans = Boolean }): Unit = ???
    }

    object StorageAlgebra extends QuestionAlgebra[AnswerStore] {
      def textQuestion(): AnswerStore[String] = StringStore

      def nameQuestion(): AnswerStore[String] = StringStore

      def booleanQuestion(): AnswerStore[Boolean] = BooleanStore
    }
  }

  object LibraryStuff {
    import Model._
    import Stores._

    // This method wants a store to do its work
    def doStuff(answer: Question)(implicit store: AnswerStore[answer.Ans]): Boolean = ???
  }

  object Example {
    import Model._
    import Stores._
    import LibraryStuff._

    val question = TextQuestion("Wat?", Some("Huh"))
    val nameQuestion = NameQuestion("What is your name?", Some("Bob"))
    val yesNo = BooleanQuestion("Huh?", Some(true))

    doStuff(question)
    doStuff(nameQuestion)
    doStuff(yesNo)

    // a list with no existential type - yay
    val questions: List[Question] = question :: nameQuestion :: yesNo :: Nil


    questions.foreach(q ⇒ q.run(StorageAlgebra).store(q))
  }
}

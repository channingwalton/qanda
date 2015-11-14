package qanda

import scala.xml.NodeSeq
import java.util.UUID

// see pchiusano.github.io/2014-05-20/scala-gadts.html
object TaglessQandA extends App {

  abstract class Question[A](val question: String, val key: String = UUID.randomUUID().toString, val validations: Seq[A ⇒ String] = Seq.empty) {
    def run[F[_]](F: QuestionAlg[F]): F[A]
  }

  trait QuestionAlg[F[_]] {
    def textQuestion(question: Question[String]): F[String]

    def booleanQuestion(question: Question[Boolean]): F[Boolean]
  }

  object Question {
    def textQuestion(question: String) = new Question[String](question) {
      def run[F[_]](F: QuestionAlg[F]): F[String] = F.textQuestion(this)
    }
    def booleanQuestion(question: String) = new Question[Boolean](question) {
      def run[F[_]](F: QuestionAlg[F]): F[Boolean] = F.booleanQuestion(this)
    }
  }

  // Answers
  trait AnswerAlg[F[_]] {
    def textAnswer(question: Question[String], answer: String): F[String]

    def booleanAnswer(question: Question[Boolean], answer: Boolean): F[Boolean]
  }

  trait Answer[A] {
    def run[F[_]](F: AnswerAlg[F]): F[A]
  }

  object Answer {
    def textAnswer(question: Question[String], answer: String): Answer[String] = new Answer[String] {
      def run[F[_]](F: AnswerAlg[F]): F[String] = F.textAnswer(question, answer)
    }

    def booleanAnswer(question: Question[Boolean], answer: Boolean): Answer[Boolean] = new Answer[Boolean] {
      def run[F[_]](F: AnswerAlg[F]): F[Boolean] = F.booleanAnswer(question, answer)
    }
  }

  // Rx
  trait Rx[+T] {
    def ns(): NodeSeq
  }

  object RxQuestionAlg extends QuestionAlg[Rx] {
    def textQuestion(question: Question[String]): Rx[String] = new Rx[String] {
      def ns() = <p id={question.key}>
        {question.question}
      </p>
    }

    def booleanQuestion(question: Question[Boolean]): Rx[Boolean] = new Rx[Boolean] {
      def ns() = <p id={question.key}>
        {question.question}
      </p>
    }
  }

  // Questions and answer Ops for Rx
  object RxAnswerAlg extends AnswerAlg[Rx] {
    def textAnswer(question: Question[String], answer: String): Rx[String] = new Rx[String] {
      def ns(): NodeSeq = <div id={question.key + "-answer"}>
        {question.run(RxQuestionAlg).ns()}<textarea>
          {answer}
        </textarea>
      </div>
    }

    def booleanAnswer(question: Question[Boolean], answer: Boolean): Rx[Boolean] = new Rx[Boolean] {
      def ns(): NodeSeq = <div id={question.key + "-answer"}>
        {question.run(RxQuestionAlg).ns()}<radio>
          {answer}
        </radio>
      </div>
    }
  }

  val answers: List[Answer[_]] = Answer.booleanAnswer(Question.booleanQuestion("Yes or no"), true) :: Answer.textAnswer(Question.textQuestion("How you doing'?"), "Ok") :: Nil

  val rx = answers.map(_.run(RxAnswerAlg).ns())
  println(rx)
}
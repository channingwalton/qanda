package typeclasses

import java.io.Serializable

import model.Address

import scala.xml.NodeSeq

/**
  * Don't represent types of questions as an ADT so that new question types can be created.
  * Represent behaviour with type classes.
  * Pull behaviours together when the questionnaire is constructed.
  */
object Typeclasses {

  // the core model of a question and behaviours
  object Core {
    type Key = String

    // unsealed trait for question
    trait Question[T] extends Product with Serializable {
      def k: Key
      def question: String
    }

    // a questionnaire is built with a question and more context
    final case class QContext[Q <: Question[_]](q: Q)(implicit html: AsHtml[Q], completion: CompletionCalculator[Q]) {
      def renderHtml: NodeSeq = html.asHtml(q)
      def completion(answerMap: AnswerMap): Completion = completion.calculate(q, answerMap)
    }

    case class Answer[T](q: Question[T], v: T)

    type AnswerMap = Map[Key, Answer[_]]

    trait AsHtml[A] {
      def asHtml(a: A): NodeSeq
    }

    sealed trait Completion extends Product with Serializable {
      // the key of the completed / incomplete question
      def key: Key
    }
    case class Complete(key: Key) extends Completion
    case class NotComplete(key: Key) extends Completion

    trait CompletionCalculator[A] {
      def calculate(a: A, answerMap: AnswerMap): Completion
    }

    object CompletionCalculator {
      def calculate(questions: List[QContext[_]], answerMap: AnswerMap): List[Completion] =
        questions.map(q ⇒ q.completion(answerMap))
    }
  }

  object Basics {
    import Core._

    // questions and companions
    final case class StringQuestion(k: Key, question: String) extends Question[String]

    object StringQuestion {

      implicit object StringAsHtml extends AsHtml[StringQuestion] {
        override def asHtml(a: StringQuestion): NodeSeq =
          <span>{a.question}:</span> ++ <input id={a.k}/>
      }

      implicit object StringCompletion extends CompletionCalculator[StringQuestion] {
        override def calculate(q: StringQuestion, answerMap: AnswerMap): Completion =
          if (answerMap.contains(q.k)) Complete(q.k) else NotComplete(q.k)
      }
    }

    final case class BooleanQuestion(k: Key, question: String) extends Question[Boolean]

    object BooleanQuestion {

      implicit object BooleanAsHtml extends AsHtml[BooleanQuestion] {
        override def asHtml(a: BooleanQuestion): NodeSeq =
          <span>{a.question}:</span> ++ <checkbox id={a.k}/>
      }

      implicit object BooleanCompletion extends CompletionCalculator[BooleanQuestion] {
        override def calculate(q: BooleanQuestion, answerMap: AnswerMap): Completion =
          if (answerMap.contains(q.k)) Complete(q.k) else NotComplete(q.k)
      }
    }
  }

  object BasicQuestionnaire {
    import Core._
    import Basics._

    val who = StringQuestion("a", "Who are you?")
    val happy = BooleanQuestion("b", "Happy?")
    val questionnaire: List[QContext[_]] = QContext(who) :: QContext(happy) :: Nil

    val ui: NodeSeq = questionnaire.map(_.renderHtml).foldLeft(NodeSeq.Empty)(_ ++ _)

    val completions: List[Completion] = CompletionCalculator.calculate(questionnaire, Map("a" → Answer(who, "Channing")))
  }

  // now extend it
  object Extensions {
    import Core._

    final case class AddressQuestion(k: Key, question: String) extends Question[Address]

    object AddressQuestion {

      implicit object AddressAsHtml extends AsHtml[AddressQuestion] {
        override def asHtml(a: AddressQuestion): NodeSeq =
          <span>
            {a.question}
            :</span> ++ <input id={s"${a.k}-line1"}/> ++ <input id={s"${a.k}-line2"}/> ++ <input id={s"${a.k}-etc"}/>
      }
      implicit object AddressCompletion extends CompletionCalculator[AddressQuestion] {
        override def calculate(q: AddressQuestion, answerMap: AnswerMap): Completion =
          if (answerMap.contains(q.k)) Complete(q.k) else NotComplete(q.k)
      }
    }
  }

  object ExtendedQuestionnaire {
    import Core._
    import BasicQuestionnaire._
    import Extensions._

    val extendedQuestionnaire = QContext(AddressQuestion("c", "Where do you live?")) :: questionnaire

    val extendedUI: NodeSeq = extendedQuestionnaire.map(_.renderHtml).foldLeft(NodeSeq.Empty)(_ ++ _)
  }
}

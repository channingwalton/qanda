package shapelessed

import shapeless._
import shapeless.ops.hlist.Mapper

import scala.xml.NodeSeq

object ShapelessSolution extends App {

  // We start with the obvious simple case class model
  type Key = String

  trait Question[T] extends Product with Serializable {
    def k: Key
    def question: String
  }
  case class StringQuestion(k: Key, question: String)  extends Question[String]
  case class BooleanQuestion(k: Key, question: String) extends Question[Boolean]

  case class Answer[T](k: Key, v: T)

  // The answers are kept in a map
  type AnswerMap = Map[Key, Answer[_]]

  // render as NodeSeq using a polymorphic function
  trait HtmlRenderer extends Poly1 {
    implicit def caseString = at[StringQuestion]((x: StringQuestion) ⇒ <input id={x.k}></input>)

    implicit def caseBoolean = at[BooleanQuestion]((x: BooleanQuestion) ⇒ <checkbox id={x.k.toString}></checkbox>)
  }

  object HtmlRenderer extends HtmlRenderer {
    // cheeky putting this here but it makes it easier to use
    def apply[L <: HList](list: L)(implicit m: Mapper[HtmlRenderer.type, L]): m.Out = list.map(HtmlRenderer)
  }

  // compute completion
  sealed trait Completion {
    def key: Key
  }

  case class Complete(key: Key)    extends Completion
  case class NotComplete(key: Key) extends Completion

  class CompletionCalculator(answers: AnswerMap) {

    object Apply extends Poly1 {
      implicit def caseString =
        at[StringQuestion]((x: StringQuestion) ⇒ answers.get(x.k).fold[Completion](NotComplete(x.k))(_ ⇒ Complete(x.k)))

      implicit def caseBoolean =
        at[BooleanQuestion]((x: BooleanQuestion) ⇒ answers.get(x.k).fold[Completion](NotComplete(x.k))(_ ⇒ Complete(x.k)))

    }
    def apply[L <: HList](list: L)(implicit m: Mapper[Apply.type, L]): m.Out = list.map(Apply)
  }

  // testing

  // A questionnaire is simply an HList
  val bQuestion: BooleanQuestion = BooleanQuestion("a", "Yes or no?")
  val tQuestion: StringQuestion  = StringQuestion("b", "What's your answer?")

  val questions = bQuestion :: tQuestion :: HNil

  val nodeSeq = HtmlRenderer(questions).toList.foldLeft(NodeSeq.Empty)(_ ++ _)
  println(nodeSeq)
  /*
  <checkbox id="a"></checkbox><input id="b"></input>
   */

  val someAnswers: AnswerMap = Map("a" -> Answer("a", "I haz answer"))
  val calc                   = new CompletionCalculator(someAnswers)
  val results                = calc(questions)

  println(results)
  /*
  Complete(a) :: NotComplete(b) :: HNil
   */

  // And we can extend it!

  case class NumberQuestion(k: Key, question: String) extends Question[Integer]

  trait ExtendedHtmlRenderer extends HtmlRenderer {
    implicit def caseNumber = at[NumberQuestion]((x: NumberQuestion) ⇒ <numberWidget id={x.k}></numberWidget>)
  }

  object ExtendedHtmlRenderer extends ExtendedHtmlRenderer {
    def apply[L <: HList](list: L)(implicit m: Mapper[ExtendedHtmlRenderer.type, L]): m.Out = list.map(ExtendedHtmlRenderer)
  }

  val extendedQuestions = bQuestion :: tQuestion :: NumberQuestion("c", "How much?") :: HNil
  val extendedNodeSeq   = ExtendedHtmlRenderer(extendedQuestions).toList.foldLeft(NodeSeq.Empty)(_ ++ _)
  println(extendedNodeSeq)
  /*
  <checkbox id="a"></checkbox><input id="b"></input><numberWidget id="c"></numberWidget>
 */
}

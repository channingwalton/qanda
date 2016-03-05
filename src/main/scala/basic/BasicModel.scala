package basic

import scala.xml.NodeSeq

/**
  * This is an implementation that is both obvious and straight forward, but not extensible.
  * There is no way to share this model and allow client code to extend it.
  *
  * The other problem is that a questionnaire, a list of questions, ends up having
  * the type List[Question[_]] that requires type rediscovery through pattern matching
  * or fold. Code ends up being littered with pattern matches - is that a bad thing?
  */
object BasicModel {

  type Key = String

  sealed trait Question[T] extends Product with Serializable {
    def k: Key
    def question: String
  }
  case class StringQuestion(k: Key, question: String) extends Question[String]
  case class BooleanQuestion(k: Key, question: String) extends Question[Boolean]

  val questionnaire: List[Question[_]] = StringQuestion("a", "Who are you?") :: BooleanQuestion("b", "Happy?") :: Nil

  // Rendering the questionnaire by pattern matching
  // we can assume that form submissions
  def html[T](question: Question[T]): NodeSeq =
    question match {
      case StringQuestion(k, q) => <span>{q}:</span> ++ <input id={k} />
      case BooleanQuestion(k, q) => <span>{q}:</span> ++ <checkbox id={k} />
    }

  val ui: NodeSeq = questionnaire.map(html(_)).foldLeft(NodeSeq.Empty)(_ ++ _)

  // implementing a measure of completion is simply a function of the set of answers
  // to the questions

  // validating questions is also a function of the set of answers so there is nothing
  // interesting in that implementation.
}

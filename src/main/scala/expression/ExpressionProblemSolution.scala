package expression

import scala.xml.NodeSeq

object ExpressionProblemSolution extends App {

  // The key to a question
  type Key = String

  case class Answer[T](k: Key, v: T)

  type AnswerMap = Map[Key, Answer[_]]

  // The basic algorithm offering simple types of questions
  trait QuestionAlg[E] {
    def string(k: Key, q: String): E
    def boolean(k: Key, q: String): E
  }

  // An Html component that renders a question
  //
  trait HtmlComponent {
    def nodeseq: NodeSeq
  }

  class HtmlInput(key: Key) extends HtmlComponent {
    override def nodeseq: NodeSeq = <input id={key.toString}></input>
  }

  class HtmlCheckbox(key: Key) extends HtmlComponent {
    override def nodeseq: NodeSeq = <checkbox id={key.toString}></checkbox>
  }

  // a QuestionAlg that can produce HtmlComponents
  trait HtmlAlg extends QuestionAlg[HtmlComponent] {
    def string(k: Key, q: String) = new HtmlInput(k)
    def boolean(k: Key, q: String) = new HtmlCheckbox(k)
  }

  // **********
  // How complete is the questionnaire?
  //
  sealed trait Completion {
    def key: Key
  }

  case class Complete(key: Key) extends Completion
  case class NotComplete(key: Key) extends Completion

  // a simple completion algorithm that requires answers to all questions
  trait CompletionAlg extends QuestionAlg[Completion] {
    def qAnswer[T](k: Key): Option[Answer[T]]

    def string(k: Key, q: String) = qAnswer[String](k).fold[Completion](NotComplete(k))(_ => Complete(k))

    def boolean(k: Key, q: String) = qAnswer[Boolean](k).fold[Completion](NotComplete(k))(_ => Complete(k))
  }


  // *********
  // Put it all together

  // a questionnaire
  def questionnaire[E](alg: QuestionAlg[E]): List[E] = {
    import alg._
    string("a", "What is your name?") :: boolean("b", "Are you over 18?") :: Nil
  }

  val someAnswers: AnswerMap = Map("a" -> Answer("a", "I haz answer"))

  // render the UI
  val uiAlg: HtmlAlg = new HtmlAlg() {}

  val initialHtml: NodeSeq = questionnaire(uiAlg).map(_.nodeseq).foldLeft(NodeSeq.Empty)(_ ++ _)

  println(initialHtml)

  // calculate completion for the questionnaire
  def calcCompletion(answers: AnswerMap): CompletionAlg = new CompletionAlg {
    override def qAnswer[T](k: Key): Option[Answer[T]] = answers.get(k).asInstanceOf[Option[Answer[T]]] //yuck - whatever
  }

  val completionResults: List[Completion] = questionnaire(calcCompletion(someAnswers))

  println(completionResults)

  // **********************
  // That's all ok but can we extend it?

  // yes

  trait ExtendedQuestionAlg[E] extends QuestionAlg[E] {
    def number(k: Key, q: String): E
  }

  def extendedQuestionnaire[E](alg: ExtendedQuestionAlg[E]): List[E] = {
    import alg._
    string("a", "What is your name?") :: boolean("b", "Are you over 18?") :: number("c", "How old are you?") :: Nil
  }

  // render the extended UI
  class HtmlNumber(key: Key) extends HtmlComponent {
    override def nodeseq: NodeSeq = <numberWidget id={key.toString}></numberWidget>
  }

  trait ExtendedHtmlAlg extends HtmlAlg with ExtendedQuestionAlg[HtmlComponent] {
    def number(k: Key, q: String) = new HtmlNumber(k)
  }

  // extend completion
  trait ExtendedCompletionAlg extends CompletionAlg with ExtendedQuestionAlg[Completion] {
    def number(k: Key, q: String) = qAnswer[Integer](k).fold[Completion](NotComplete(k))(_ => Complete(k))
  }

  def calcExtendedCompletion(answers: AnswerMap): ExtendedCompletionAlg = new ExtendedCompletionAlg {
    override def qAnswer[T](k: Key): Option[Answer[T]] = answers.get(k).asInstanceOf[Option[Answer[T]]] //yuck - whatever
  }

  val extendedHtml: ExtendedHtmlAlg = new ExtendedHtmlAlg() {}

  val extendedHtmlUI: List[HtmlComponent] = extendedQuestionnaire(extendedHtml)

  println("Extended UI:")
  println(extendedHtmlUI.foldLeft(NodeSeq.Empty)(_ ++ _.nodeseq))

  val extendedCompletionResults: List[Completion] = extendedQuestionnaire(calcExtendedCompletion(someAnswers))

  println(extendedCompletionResults)
}
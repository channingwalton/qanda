package qanda

import rx.lang.scala.Observable

import scala.xml.NodeSeq

object ExpProbSolutionWithAnswers extends App {

  // The key to a question
  type Key = String

  case class Answer[T](k: Key, v: T)

  type AnswerMap = Map[Key, Answer[_]]

  // The basic algorithm offering simple types of questions
  trait QuestionAlg[E] {
    def string(k: Key, q: String): E
    def boolean(k: Key, q: String): E
  }

  // **********
  // A Rx (reactive) UI component that consumes a stream of answers to render
  // and emits JsCmds from the UI component to push to a browser via comet
  // see https://github.com/channingwalton/rxlift
  trait JsCmd
  trait RxComponent {
    // necessary to use an abstract type to avoid having to make Eval covariant - try it and see
    type T

    // This is the incoming stream of answer that should be mapped to produce the jscmd stream below
    def in: Observable[Option[Answer[T]]]

    def nodeseq: NodeSeq

    def emitted: Observable[Option[Answer[T]]]

    def jscmd: Observable[JsCmd]
  }

  class RxInput(key: Key, val in: Observable[Option[Answer[String]]]) extends RxComponent {
    type T = String

    override def nodeseq: NodeSeq = <input id={key.toString}></input>

    override def jscmd: Observable[JsCmd] = Observable.empty

    override def emitted: Observable[Option[Answer[String]]] = Observable.empty
  }

  class RxCheckbox(key: Key, val in: Observable[Option[Answer[Boolean]]]) extends RxComponent {
    type T = Boolean

    override def nodeseq: NodeSeq = <checkbox id={key.toString}></checkbox>

    override def jscmd: Observable[JsCmd] = Observable.empty

    override def emitted: Observable[Option[Answer[Boolean]]] = Observable.empty
  }

  // a QuestionAlg that can produce RxComponents
  trait RxAlg extends QuestionAlg[RxComponent] {

    // an observable stream of answers for a given key
    def answers[T](k: Key): Observable[Option[Answer[T]]]

    def string(k: Key, q: String) = new RxInput(k, answers(k))

    def boolean(k: Key, q: String) = new RxCheckbox(k, answers(k))
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
  val rx: RxAlg = new RxAlg() {
    // in practice the observable will be a stream coming from the persistence
    override def answers[T](k: Key): Observable[Option[Answer[T]]] = Observable.empty
  }

  // build the UI
  val rxUI: List[RxComponent] = questionnaire(rx)

  val initialHtml: NodeSeq = rxUI.map(_.nodeseq).foldLeft(NodeSeq.Empty)(_ ++ _)
  val javascriptStream = rxUI.map(_.jscmd).reduce(_.merge(_))

  println(initialHtml)

  // now the initial presentation of the UI is in initialHtml and the updates to send the browser are in javscriptStream

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
  class RxNumber(key: Key, val in: Observable[Option[Answer[Integer]]]) extends RxComponent {
    type T = Integer

    override def nodeseq: NodeSeq = <numberWidget id={key.toString}></numberWidget>

    override def jscmd: Observable[JsCmd] = Observable.empty

    override def emitted: Observable[Option[Answer[Integer]]] = Observable.empty
  }

  trait ExtendedRxAlg extends RxAlg with ExtendedQuestionAlg[RxComponent] {
    def number(k: Key, q: String) = new RxNumber(k, answers(k))
  }

  val extendedRx: ExtendedRxAlg = new ExtendedRxAlg() {
    // in practice the observable will be a stream coming from the persistence
    override def answers[T](k: Key): Observable[Option[Answer[T]]] = Observable.empty
  }

  val extendedRxUI: List[RxComponent] = extendedQuestionnaire(extendedRx)

  println("Extended:")
  println(extendedRxUI.foldLeft(NodeSeq.Empty)(_ ++ _.nodeseq))

  // extend completion
  trait ExtendedCompletionAlg extends CompletionAlg with ExtendedQuestionAlg[Completion] {
    def number(k: Key, q: String) = qAnswer[Integer](k).fold[Completion](NotComplete(k))(_ => Complete(k))
  }

  def calcExtendedCompletion(answers: AnswerMap): ExtendedCompletionAlg = new ExtendedCompletionAlg {
    override def qAnswer[T](k: Key): Option[Answer[T]] = answers.get(k).asInstanceOf[Option[Answer[T]]] //yuck - whatever
  }

  val extendedCompletionResults: List[Completion] = extendedQuestionnaire(calcExtendedCompletion(someAnswers))

  println(extendedCompletionResults)
}
package qanda

object QuestionsAndAnswers extends App {

  import scala.xml.NodeSeq

  // The Questions

  sealed trait Question[T] {
    def question: String
  }

  case class TextQuestion(question: String) extends Question[String]
  case class BooleanQuestion(question: String) extends Question[Boolean]

  // Ops typeclass for Questions
  trait Ops[T] {
    def text(q: TextQuestion): T
    def boolean(q: BooleanQuestion): T
  }

  // a method to apply Ops to a question
  object OpsApply {
    def apply[T: Ops](question: Question[_]): T = {
      val ops = implicitly[Ops[T]]
      question match {
        case q: TextQuestion ⇒ ops.text(q)
        case q: BooleanQuestion ⇒ ops.boolean(q)
      }
    }
  }

  // pretend RxElement
  type Rx = String

  // Question Ops on an RxElement
  implicit object ToRxOps extends Ops[Rx] {
    def text(q: TextQuestion): Rx = "Text"
    def boolean(q: BooleanQuestion): Rx = "Boolean"
  }

  // another transformation
  implicit object ToXmlOps extends Ops[NodeSeq] {
    def text(q: TextQuestion): NodeSeq = <text/>
    def boolean(q: BooleanQuestion): NodeSeq = <boolean/>
  }

  // try it

  // some questions
  val bQuestion: BooleanQuestion = BooleanQuestion("Yes or no")
  val tQuestion: TextQuestion = TextQuestion("What's your answer")
  val qList: List[Question[_]] = bQuestion :: tQuestion :: Nil

  // apply Ops of different types to the questions
  println(qList map OpsApply[Rx])
  println(qList map OpsApply[NodeSeq])

  // answers
  case class Answer[V](question: Question[V], value: V)

  val answers: List[Answer[_]] = Answer(bQuestion, true) :: Answer(tQuestion, "yup") :: Nil

  trait AnswerOps[T] {
    def text(a: String): T
    def boolean(b: Boolean): T
  }

  // Something to apply AnswerOps to answers
  object AnswerOpsApply {
    def apply[T: AnswerOps](answer: Answer[_]): T = {
      val aops = implicitly[AnswerOps[T]]
      answer match {
        case Answer(q: TextQuestion, v: String) ⇒ aops.text(v)
        case Answer(q: BooleanQuestion, v: Boolean) ⇒ aops.boolean(v)
      }
    }
  }

  implicit def ToRxAnswerOps = new AnswerOps[Rx] {
    def text(a: String): Rx = "Text Answer"
    def boolean(b: Boolean): Rx = "Boolean Answer"
  }

  println(answers map AnswerOpsApply[Rx])
}

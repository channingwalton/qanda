package typeclasses

import model.Address

import scala.xml.NodeSeq

/**
  * Don't represent types of questions as an ADT so that new question types can be created.
  * Represent behaviour with type classes.
  * Pull behaviours together when the questionnaire is constructed.
  */
object Typeclasses {

  type Key = String

  object Basics {

    // unsealed trait for question
    trait Question[T] extends Product with Serializable {
      def k: Key
      def question: String
    }

    // A type class for rendering as html
    trait AsHtml[A] {
      def asHtml(a: A): NodeSeq
    }

    // questions and companions
    final case class StringQuestion(k: Key, question: String) extends Question[String]

    object StringQuestion {

      implicit object StringAsHtml extends AsHtml[StringQuestion] {
        override def asHtml(a: StringQuestion): NodeSeq =
          <span>
            {a.question}
            :</span> ++ <input id={a.k}/>
      }

    }

    final case class BooleanQuestion(k: Key, question: String) extends Question[Boolean]

    object BooleanQuestion {

      implicit object BooleanAsHtml extends AsHtml[BooleanQuestion] {
        override def asHtml(a: BooleanQuestion): NodeSeq =
          <span>
            {a.question}
            :</span> ++ <checkbox id={a.k}/>
      }

    }
  }

  import Basics._
  // build a questionnaire with instance of the typeclass instances required
  final case class QContext[Q <: Question[_]](q: Q)(implicit html: AsHtml[Q]) {
    def renderHtml: NodeSeq = html.asHtml(q)
  }

  val questionnaire: List[QContext[_]] =
    QContext(StringQuestion("a", "Who are you?")) ::
      QContext(BooleanQuestion("b", "Happy?")) ::
      Nil

  val ui: NodeSeq = questionnaire.map(_.renderHtml).foldLeft(NodeSeq.Empty)(_ ++ _)

  // now extend it
  object Extensions {
    import Basics._
    final case class AddressQuestion(k: Key, question: String) extends Question[Address]

    object AddressQuestion {

      implicit object AddressAsHtml extends AsHtml[AddressQuestion] {
        override def asHtml(a: AddressQuestion): NodeSeq =
          <span>
            {a.question}
            :</span> ++ <input id={s"${a.k}-line1"}/> ++ <input id={s"${a.k}-line2"}/> ++ <input id={s"${a.k}-etc"}/>
      }

    }
  }

  import Extensions._
  val extendedQuestionnaire = QContext(AddressQuestion("c", "Where do you live?")) :: questionnaire

  val extendedUI: NodeSeq = extendedQuestionnaire.map(_.renderHtml).foldLeft(NodeSeq.Empty)(_ ++ _)
}

package qanda

import scala.xml.{NodeSeq, Elem}

/*
 * Based on http://i.cs.hku.hk/~bruno/oa/
 *
 * This solution starts with an initial algebra offering String and Boolean question types,
 * then extends it by adding numbers.
 * Implementations of the algebra to generate HTML and two types of printing is also shown and extended.
 */
object ExpressionProblemSolution extends App {

  // Initial object algebra interface for questions: string and boolean
  trait QuestionAlg[E] {
    def string(q: String) : E
    def boolean(q: String) : E
  }

  // An object algebra implementing that interface (evaluation)

  // The evaluation interface
  trait Eval[T] {
    def eval() : T
  }

  // The object algebra for HTML
  trait HtmlAlg extends QuestionAlg[Eval[Elem]] {
    def string(q: String) = new Eval[Elem]() {
      override def eval() = <p>{q}</p>
    }

    def boolean(q: String) = new Eval[Elem]() {
      override def eval() = <p>{q}</p>
    }
  }

  // Evolution 1: Adding number to the algebra
  trait IntegerQuestionAlg[E] extends QuestionAlg[E] {
    def number(q: String) : E
  }

  // A new Html algebra including the Integer question
  trait HtmlSubAlg extends HtmlAlg with IntegerQuestionAlg[Eval[Elem]] {
    def number(q: String) = new Eval[Elem]() {
      override def eval() = <p>{q}</p>
    }
  }

  // Evolution 2: Adding pretty printing
  trait PPrint {
    def print() : String
  }

  trait PrintQuestionAlg extends IntegerQuestionAlg[PPrint] {
    def string(q: String) = new PPrint {
      override def print(): String = q
    }
    def boolean(q: String) = new PPrint {
      override def print(): String = q
    }
    def number(q: String) = new PPrint {
      override def print(): String = q
    }
  }

  // An alternative object algebra for pretty printing:
  trait PrintQuestionAlg2 extends IntegerQuestionAlg[String] {
    def string(q: String): String = q.toUpperCase
    def boolean(q: String): String = q.toUpperCase
    def number(q: String): String = q.toUpperCase
  }

  // Testing

  // a questionnaire
  def questionnaire[E](alg : IntegerQuestionAlg[E]): List[E] = {
    import alg._
    string("What is your name?") :: boolean("Are you over 18?") :: number("How old are you?") :: Nil
  }

  def test() : Unit = {
    // Some object algebras:
    val html: HtmlSubAlg = new HtmlSubAlg() {}
    val pa: PrintQuestionAlg = new PrintQuestionAlg() {}
    val pa2: PrintQuestionAlg2 = new PrintQuestionAlg2() {}

    // dealing with a collection
    println("Printed \"" + questionnaire(pa).map(_.print()).mkString(","))
    println("As HTML is " + questionnaire(html).map(_.eval()).foldLeft(NodeSeq.Empty)(_ ++ _))
    println("Alternative printing \"" + questionnaire(pa2).mkString(",") + "\"")
  }

  test()
}
package tree

import cats.data.Xor
import cats.data.Xor.{Left, Right}
import cats.syntax.xor._

/**
  * Here the questionnaire is represented as a forest, QNode,
  * whose leaves are primitive types (String, Ints, etc.).
  *
  * Nodes of the forect are either an optional value, an answer,
  * or a Vector of QNodes.
  *
  * Each node in the forest has a key, so that parts of a questionnaire
  * and answers can be found via a path.
  *
  * This solution is extensible since answers are composites of
  * simple primitives. But, the huge drawback is a lack of rich types such
  * as Address or PhoneNumber.
  *
  * Unlike the other solutions, the answers to the questionnaire are
  * embedded in the questionnaire itself, there is no collection of
  * answers.
  */
object Tree extends App {

  // Answers are only primitive types - we will just have String and Int for now
  sealed trait Answer extends Product with Serializable

  final case class StringAnswer(v: String) extends Answer

  final case class IntAnswer(v: Int) extends Answer

  type Element = Option[Answer] Xor Vector[QNode]

  case class QNode(key: String, text: String, element: Element)

  /**
    * Answer a question at a path under the given qnode
    *
    * @return None if the path did not match or Some modified questionnaire
    */
  def answer(node: QNode, path: String*)(value: Answer): Option[QNode] =
    path.toList match {
      case s :: Nil if node.key == s ⇒ Some(node.copy(element = Some(value).left))
      case s :: Nil ⇒ None
      case a :: b if node.key == a ⇒
        node.element match {
          case Left(_) ⇒ None // value present at this incomplete path location
          case Right(qnodes) ⇒
            val updated = for {
              q ← qnodes
              u = answer(q, b: _*)(value).getOrElse(q)
            } yield u

            if (updated == qnodes)
              None
            else
              Some(node.copy(element = Right(updated)))
        }
      case _ ⇒ None
    }

  /*
   *  string representation of a QNode
   */
  def show(questionnaire: QNode): String = {
    def s(q: QNode, d: String): String =
      d + q.text + {
        q.element match {
          case Left(Some(ans)) ⇒ d + " " + ans.toString
          case Left(_) ⇒ ""
          case Right(questions) ⇒ "\n" + questions.map(q ⇒ s(q, d + " ")).mkString("\n")
        }
      }
    s(questionnaire, "")
  }

  ////// An Example

  val address: QNode =
    QNode("address", "Address",
      Right(
        Vector(
          QNode("line1", "Line 1", Left(None)),
          QNode("line2", "Line 2", Left(None)),
          QNode("fromDate", "From", Left(None))
        )
      )
    )

  val firstName: QNode = QNode("firstName", "First name", Left(None))
  val lastName: QNode = QNode("lastName", "Last name", Left(None))
  val age: QNode = QNode("age", "Age", Left(None))

  val questionnaire: QNode =
    QNode(
      "personalQuestions",
      "Personal Questions",
      Right(Vector(firstName, lastName, address, age))
    )

  // answer some questions - the for comprehension is convenience really, it
  // could be done line by line
  val answered: Option[QNode] =
    for {
      a1 ← answer(questionnaire, "personalQuestions", "address", "line1")(StringAnswer("14 Orchid Drive"))
      a2 ← answer(a1, "personalQuestions", "firstName")(StringAnswer("Channing"))
      a3 ← answer(a2, "personalQuestions", "age")(IntAnswer(48))
    } yield a3

  println(answered.fold("No answer")(show))

  /*
  Personal Questions
   First name  StringAnswer(Channing)
   Last name
   Address
    Line 1   StringAnswer(14 Orchid Drive)
    Line 2
    From
   Age  IntAnswer(48)
   */


  // TODO completion
}


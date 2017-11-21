package tree

/**
  * Here the questionnaire is represented as a tree, QNode,
  * whose leaves are primitive types (String, Ints, etc.).
  *
  * Nodes of the tree are either an optional value, an answer,
  * or a Vector of QNodes.
  *
  * Each node in the tree has a key, so that parts of a questionnaire
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

  type Element = Option[Answer] Either Children

  final case class Children(nodes: Vector[QNode])

  /**
    * QNode is a node in the questionnaire tree
    * @param key the key enables paths to be formed and uniquely describes this node in the questionnaire
    * @param text displayed for the node
    * @param element either an answer or more children
    * @param trans a function to apply to the children of this node when a question is answered.
    *              This enables repeating questions, such as addresses or phone numbers.
    */
  case class QNode(key: String, text: String, element: Element, trans: Children => Children = identity) {
    def transform: QNode =
      copy(element = element.map(trans))
  }

  /**
    * Answer a question at a path under the given qnode and apply node.trans to the result
    *
    * @return None if the path did not match or there was no change in the answer, or Some modified questionnaire
    */
  def answer(node: QNode, path: String*)(value: Answer): Option[QNode] =
    path.toList match {
      case s :: Nil if node.key == s ⇒
        Some(node.copy(element = Left(Some(value))))
      case _ :: Nil ⇒ None
      case a :: subpath if node.key == a ⇒
        node.element match {
          case Left(_) ⇒ None // there is an answer present at this incomplete path location - we were expecting children
          case Right(Children(qnodes)) ⇒
            val updated = for {
              q ← qnodes
              u = answer(q, subpath: _*)(value).getOrElse(q)
            } yield u

            if (updated == qnodes)
              None
            else
              Some(node.copy(element = Right(Children(updated))).transform)
        }
      case _ ⇒ None
    }

  /*
   *  string representation of a QNode
   */
  def show(questionnaire: QNode): String = {
    def toString(q: QNode, d: String): String =
      q.element match {
        case Left(Some(ans))            ⇒ d + " " + ans.toString
        case Left(_)                    ⇒ ""
        case Right(Children(questions)) ⇒ "\n" + questions.map(q ⇒ s(q, d + " ")).mkString("\n")
      }

    def s(q: QNode, d: String): String =
      d + q.text + toString(q, d)

    s(questionnaire, "")
  }

  ////// An Example

  val address: QNode =
    QNode(
      "address",
      "Address",
      Right(
        Children(
          Vector(
            QNode("line1", "Line 1", Left(None)),
            QNode("line2", "Line 2", Left(None)),
            QNode("fromDate", "From", Left(None))
          )
        )
      )
    )

  val firstName: QNode = QNode("firstName", "First name", Left(None))
  val lastName: QNode  = QNode("lastName", "Last name", Left(None))
  val age: QNode       = QNode("age", "Age", Left(None))

  val questionnaire: QNode =
    QNode(
      "personalQuestions",
      "Personal Questions",
      Right(Children(Vector(firstName, lastName, address, age)))
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

  // Repeating questions
  val phone: QNode = QNode("phone", "Phone Number", Left(None))

  // This function appends a new child node for the next number when a question is answered
  private val appendNumber: Children ⇒ Children = (children: Children) ⇒
    Children(children.nodes :+ QNode("phone" + (children.nodes.length + 1).toString, "Phone Number", Left(None)))

  val repQuestionnaire: QNode =
    QNode(
      "numbers",
      "Phone numbers",
      Right(Children(Vector(QNode("phone1", "Phone Number", Left(None))))),
      appendNumber
    )

  val repAnswered = for {
    a1 ← answer(repQuestionnaire, "numbers", "phone1")(StringAnswer("123"))
    a2 ← answer(a1, "numbers", "phone2")(StringAnswer("456"))
  } yield a2

  println(repAnswered.fold("No answer")(show))
  /*
  Phone numbers
   Phone Number  StringAnswer(123)
   Phone Number  StringAnswer(456)
   Phone Number
   */

  // TODO completion - probably a set of functions of paths
}

package qanda

import scalaz.{-\/, \/-, ∨}

object TreeQAndA extends App {

  sealed trait Answer extends Product with Serializable
  case class StringAnswer(v: String) extends Answer

  type Element = Option[Answer] ∨ Vector[Question]

  case class Question(k: Symbol, text: String, element: Element)

  /**
   * Answer a question at a path
   * @return None if the path did not match or Some modified questionnaire
   */
  def answer(questionnaire: Question, path: Symbol*)(value: Answer): Option[Question] =
    path.toList match {
      case s :: Nil if questionnaire.k == s ⇒ Some(questionnaire.copy(element = -\/(Some(value))))
      case s :: Nil ⇒ None
      case a :: b if questionnaire.k == a ⇒
        questionnaire.element match {
          case -\/(_) ⇒ None
          case \/-(questions) ⇒
            val updated = for {
              q ← questions
              u = answer(q, b:_*)(value).getOrElse(q)
            } yield u
            if (updated == questions) None else Some(questionnaire.copy(element = \/-(updated)))
        }
      case _ ⇒ None
    }

  def show(questionnaire: Question): String = {
    def s(q: Question, d: String): String =
      d + q.text + {
        q.element match {
          case -\/(Some(ans)) ⇒ d + " " + ans.toString
          case -\/(_) ⇒ ""
          case \/-(questions) ⇒ "\n" + questions.map(q ⇒ s(q, d + " ")).mkString("\n")
        }
      }
    s(questionnaire, "")
  }

  val address =
    Question('address, "Address",
      \/-(
        Vector(
          Question('line1, "Line 1", -\/(None)),
          Question('line2, "Line 2", -\/(None)),
          Question('fromDate, "From", -\/(None))
        )
      )
    )

  val firstName = Question('firstName, "First name", -\/(None))
  val lastName = Question('lastName, "Last name", -\/(None))

  val questionnaire =
    Question(
      'personalQuestions,
      "Personal Questions",
      \/-(Vector(firstName, lastName, address))
    )


  val answered =
    for {
      a1 ← answer(questionnaire, 'personalQuestions, 'address, 'line1)(StringAnswer("14 Orchid Drive"))
      a2 ← answer(a1, 'personalQuestions, 'firstName)(StringAnswer("Channing"))
    } yield a2

  println(answered.fold("No answer")(show))
  /*
   Personal Questions
   First name  StringAnswer(Channing)
   Last name
   Address
    Line 1   StringAnswer(14 Orchid Drive)
    Line 2
    From
   */
}

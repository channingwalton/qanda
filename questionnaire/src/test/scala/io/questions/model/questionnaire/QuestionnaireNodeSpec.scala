package io.questions.model.questionnaire

import cats.data.NonEmptyList
import cats.syntax.show._
import io.questions.QuestionsSpec
import io.questions.derivedvalues.JsonEncoder
import io.questions.model.questionnaire.PrimitiveAnswer.{ IntAnswer, StringAnswer }
import io.questions.model.questionnaire.TestQuestionnaire._
import io.questions.model.questionnaire.nodekey.{ NodeKey, NodeKeyExtension }

class QuestionnaireNodeSpec extends QuestionsSpec {
  private val rootKey              = NodeKey.random
  private val childKey             = NodeKey.random
  private val repeatingChildKey    = NodeKey.random
  private val repeatingElement1Key = NodeKey.random
  private val repeatingElement2Key = NodeKey.withRandomExtension(repeatingElement1Key.base)

  private val q = nonRepeatingParent(
    rootKey,
    qn(childKey, ""),
    QuestionnaireNode(repeatingChildKey,
                      "a",
                      "text",
                      Element.Parent(true, NonEmptyList.of(qn(repeatingElement1Key, ""), qn(repeatingElement2Key, ""))))
  )

  "collect keys" in {
    q.collectKeys mustBe
    Set(rootKey, childKey, repeatingChildKey, repeatingElement1Key, repeatingElement2Key)
  }

  "collect answered" in {
    // use the firstSection to avoid the metadata, enumeration, etc.
    val qne =
      for {
        q0 <- firstSection.answer(firstName.key, PrimitiveAnswer.StringAnswer(Some("bob")))
        q1 ← q0.answer(lastName.key, PrimitiveAnswer.StringAnswer(Some("Smith")))
      } yield q1

    val qn = qne.getOrElse(fail())
    val expected = Set(
      (firstName.key, PrimitiveAnswer.StringAnswer(Some("bob"))),
      (lastName.key, PrimitiveAnswer.StringAnswer(Some("Smith")))
    )
    qn.collectAnswered mustEqual expected
  }

  "foldLeft" in {
    q.foldLeft(Set.empty[NodeKey])(_ + _.key) mustBe
    Set(rootKey, childKey, repeatingChildKey, repeatingElement1Key, repeatingElement2Key)
  }

  "getAnswer" - {
    "an existing answer" in {
      val key = (filledQuestionnaire /-/ "homeAddresses" / 1 / "line1").key
      filledQuestionnaire.getAnswer(key) mustBe Right(StringAnswer(Some("Bloomfield Close")))
    }

    "bad key" in {
      val badKey = NodeKey.random
      filledQuestionnaire.getAnswer(badKey).left.value must startWith(show"Key $badKey is unknown")
    }
  }

  "getAnswerAs" - {
    "an existing answer" in {
      val key = (filledQuestionnaire /-/ "homeAddresses" / 1 / "line1").key
      filledQuestionnaire.getAnswerAs[StringAnswer](key) mustBe Right(
        StringAnswer(Some("Bloomfield Close"))
      )
    }

    "bad key" in {
      val badKey = NodeKey.random
      filledQuestionnaire.getAnswerAs[StringAnswer](badKey).left.value must startWith(show"Key $badKey is unknown")
    }

    "wrong type" in {
      val key = (filledQuestionnaire /-/ "homeAddresses" / 1 / "line1").key
      filledQuestionnaire.getAnswerAs[IntAnswer](key) mustBe Left(show"Unable to cast answer for $key")
    }
  }

  "answer" - {
    "a question deep in a tree" in {
      questionnaire.answer(firstName.key, StringAnswer(Some("bob"))).right.value.getAnswer(firstName.key) mustBe Right(
        StringAnswer(Some("bob"))
      )
    }
    "bad key returns the questionnaire unchanged" in {
      questionnaire
        .answer(NodeKey.random, StringAnswer(Some("bob")))
        .right
        .value
        .getAnswer(firstName.key) mustBe Right(StringAnswer(None))
    }
  }

  "appendChild" - {
    "adds a new child with answer blanked" in {
      val addresses = questionnaire
        .answerByPath(_ /-/ "homeAddresses" / 0 / "line1", StringAnswer(Some("14 Orchid Drive")))
        .right
        .value
        .appendChild(homeAddresses.key, NodeKeyExtension.random)
        .right
        .value
        .find(homeAddresses.key)
        .getOrElse(fail())
        .element
        .parent
        .children

      addresses.size mustBe 2
      addresses.tail.head.element.parent.children.head.element.primitive.answer.asInstanceOf[StringAnswer].answer mustBe None
    }
  }

  "derive" - {
    "with no derived anwers" in {
      val q = qn("root", "a")
      q.derive mustBe q
    }

    "with uncalculable derived answers" in {
      val q = qn(
        "root",
        intqn("child1", 1),
        derivedIntqn("child2", """|var apply = function(a, k, ke) {
             |  return insufficientData("Boo!")
             |}""".stripMargin)
      )

      q.derive mustBe q
    }

    "with a derived answer" in {
      val q = qn(
        "root",
        intqn("child1", 1),
        derivedIntqn("child2", """|var apply = function(a, k, ke) {
             |  return intResult(a.root.child1 * 2)
             |}""".stripMargin)
      )

      // Comparing JSON because comparing the objects is difficult
      JsonEncoder(q.derive) mustBe parseUnsafe(
        """|{
           |  "root" : {
           |    "child1" : 1,
           |    "child2" : 2
           |  }
           |}
        """.stripMargin
      )
    }

    "with cascading derived answers" in {
      val q = qn(
        "root",
        intqn("child1", 1),
        derivedIntqn(
          "child2",
          """|var apply = function(a, k, ke) {
             |  if (a.root.child3 == null)
             |    return insufficientData("Boo!")
             |  else
             |    return intResult(a.root.child3 * 2)
             |}""".stripMargin
        ),
        derivedIntqn("child3", """|var apply = function(a, k, ke) {
             |  return intResult(a.root.child1 * 2)
             |}""".stripMargin)
      )

      // Comparing JSON because comparing the objects is difficult
      JsonEncoder(q.derive) mustBe parseUnsafe(
        """|{
           |  "root" : {
           |    "child1" : 1,
           |    "child2" : 4,
           |    "child3" : 2
           |  }
           |}
        """.stripMargin
      )
    }
  }
}

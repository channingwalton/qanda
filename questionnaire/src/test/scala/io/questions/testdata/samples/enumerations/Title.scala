package io.questions.testdata.samples.enumerations

import cats.data.NonEmptyList
import io.questions.model.questionnaire.{ EnumerationName, EnumerationValue }

object Title {
  val name = EnumerationName("Title")

  val values: NonEmptyList[EnumerationValue] = NonEmptyList
    .of(
      "Mr",
      "Mrs",
      "Miss",
      "Ms",
      "Dr",
      "Admiral",
      "Ambassador",
      "Archbishop",
      "Asst. Commissioner",
      "Baron",
      "Baroness",
      "Baronet",
      "Bishop",
      "Brother",
      "Captain",
      "Cardinal",
      "Chief",
      "Chief Inspector",
      "Colonel",
      "Count",
      "Countess",
      "Dame",
      "Dean",
      "Duchess",
      "Duke",
      "Earl",
      "Father",
      "General",
      "Governor",
      "Her Excellency",
      "Her Royal Highness",
      "His Royal Highness",
      "HRH Prince",
      "HRH Princess",
      "Judge",
      "Lady",
      "Lord",
      "Lieutenant",
      "Madam",
      "Madame",
      "Mademoiselle",
      "Major",
      "Major General",
      "Mayor",
      "Mayoress",
      "President",
      "Prince",
      "Princess",
      "Professor",
      "Rabbi",
      "Reverend",
      "Right Reverend",
      "Sheikh",
      "Sir",
      "The Right Honourable",
      "Viscount",
      "Viscountess"
    )
    .map(EnumerationValue.of)
}

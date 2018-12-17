package io.questions.model.questionnaire
import cats.data.NonEmptyList
import shapeless.syntax.typeable._
import cats.syntax.show._
import io.questions.model.questionnaire.nodekey.NodeKey

object Components {
  val metadataKey: NodeKey     = NodeKey("26b84b20-36cf-4faa-969b-2cc17d58d9d8")
  val idKey: NodeKey           = NodeKey("117ef146-bef9-410e-a04e-ede5f5dd9deb")
  val enumerationsKey: NodeKey = NodeKey("81e3452c-425c-11e8-842f-0ed5f89f718b")

  def enumerations(enums: Map[EnumerationName, NonEmptyList[EnumerationValue]]): QuestionnaireNode =
    QuestionnaireNode(
      enumerationsKey,
      FieldName("enumerations"),
      QuestionText("enumerations"),
      Element.Enumerations(enums)
    )

  private def noSuchEnumeration(name: EnumerationName, enums: Map[EnumerationName, NonEmptyList[EnumerationValue]]): String =
    s"Enumerations QuestionnaireNode does not contain ${name.show}. Available enumerations are: ${enums.keys.map(_.show).mkString(", ")}"

  def enumerationValues(root: QuestionnaireNode, name: EnumerationName): Either[String, NonEmptyList[EnumerationValue]] =
    for {
      node    ← root.find(enumerationsKey)
      element ← node.element.cast[Element.Enumerations].toRight(s"Enumerations QuestionnaireNode has the wrong type: ${node.show}")
      enums = element.enums
      enumeration ← enums.get(name).toRight(noSuchEnumeration(name, enums))
    } yield enumeration
}

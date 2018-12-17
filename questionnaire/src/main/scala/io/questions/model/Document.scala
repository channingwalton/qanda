package io.questions.model

case class Document(id: String, mimeType: String, documentData: Array[Byte], fileName: Option[String]) {

  override def equals(other: Any): Boolean = other match {
    case that: Document => id.equals(that.id)
    case _              => false
  }

  override def hashCode: Int = id.hashCode
}

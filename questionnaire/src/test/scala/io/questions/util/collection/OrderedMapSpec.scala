package io.questions.util.collection

import io.circe.Decoder.Result
import io.circe.syntax._
import io.circe.{ Encoder, Json }
import io.questions.QuestionsSpec
import org.scalacheck.Gen

import scala.collection.immutable.ListMap

class OrderedMapSpec extends QuestionsSpec {

  implicit def encodeFoo[A: Encoder, B: Encoder]: Encoder[ListMap[A, B]] = (a: ListMap[A, B]) => {
    val elems = a.iterator.map { case (k, v) ⇒ (k.toString, v.asJson) }.toList
    Json.obj(elems: _*)
  }

  "JSON encode then decode preserve order" in {
    forAll(Gen.nonEmptyMap(pair(Gen.chooseNum(1, 1000), Gen.chooseNum(0, 1000)))) { testMap ⇒
      val sorted     = testMap.toList.sortBy(_._1).map { case (k, v) ⇒ (k.toString, v) }
      val sortedKeys = sorted.map(_._1)
      val map        = ListMap(sorted: _*)

      map.keys.toList mustEqual sortedKeys

      val json = map.asJson

      val jsonKeyPosition = sorted.map {
        case (k, _) ⇒
          val index = json.spaces2.indexOf(s""""$k"""")
          index must be >= 0
          index
      }
      jsonKeyPosition mustEqual jsonKeyPosition.sorted

      val map2: Result[ListMap[String, Int]] = json.as[ListMap[String, Int]]
      map2.right.value mustBe map
      map2.right.value.keys.toList mustEqual sortedKeys
    }
  }
}

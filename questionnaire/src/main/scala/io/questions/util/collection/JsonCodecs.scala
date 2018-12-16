/*
 * Copyright 2018 TBD
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.questions.util.collection

import cats.data.NonEmptyList
import io.circe._
import io.circe.syntax._

object JsonCodecs {
  implicit def nonEmptyListEncoder[T: Encoder]: Encoder[NonEmptyList[T]] =
    (a: NonEmptyList[T]) => a.toList.asJson

  implicit def nonEmptyListDecoder[T: Decoder](implicit ld: Decoder[List[T]]): Decoder[NonEmptyList[T]] =
    (c: HCursor) => ld(c).map(NonEmptyList.fromListUnsafe)
}

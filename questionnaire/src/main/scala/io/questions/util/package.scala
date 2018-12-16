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

package io.questions

import java.time._

import cats.Eq

package object util {
  implicit class BooleanSyntax(b: Boolean) {
    def toEither[F, T](f: ⇒ F, t: ⇒ T): Either[F, T] = if (b) Right(t) else Left(f)
  }

  implicit val localDateEq: Eq[LocalDate]         = Eq.fromUniversalEquals
  implicit val localDateTimeEq: Eq[LocalDateTime] = Eq.fromUniversalEquals
  implicit val localTimeEq: Eq[LocalTime]         = Eq.fromUniversalEquals
  implicit val zonedDateTimeEq: Eq[ZonedDateTime] = Eq.fromUniversalEquals
  implicit val offsetTimeEq: Eq[OffsetTime]       = Eq.fromUniversalEquals
  implicit val zoneIdEq: Eq[ZoneId]               = Eq.fromUniversalEquals

  implicit val localDateTimeOrdering: Ordering[LocalDateTime] = (x: LocalDateTime, y: LocalDateTime) => x.compareTo(y)
  implicit val zonedDateTimeOrdering: Ordering[ZonedDateTime] = (x: ZonedDateTime, y: ZonedDateTime) => x.compareTo(y)
  implicit val zoneIdOrdering: Ordering[ZoneId]               = (x: ZoneId, y: ZoneId) => x.getId.compareTo(y.getId)
}

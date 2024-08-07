package pl.iterators.kebs.circe

import java.time.ZonedDateTime
import enumeratum.values.{LongEnum, LongEnumEntry}
import enumeratum.{Enum, EnumEntry}
import pl.iterators.kebs.core.enums.ValueEnumLikeEntry

package object model {

  sealed abstract class LongGreeting(val value: Long) extends LongEnumEntry with ValueEnumLikeEntry[Long]

  object LongGreeting extends LongEnum[LongGreeting] {
    val values = findValues

    case object Hello   extends LongGreeting(0L)
    case object GoodBye extends LongGreeting(1L)
    case object Hi      extends LongGreeting(2L)
    case object Bye     extends LongGreeting(3L)
  }

  sealed trait Greeting extends EnumEntry

  object Greeting extends Enum[Greeting] {
    val values = findValues

    case object Hello   extends Greeting
    case object GoodBye extends Greeting
    case object Hi      extends Greeting
    case object Bye     extends Greeting
  }

  case class C(anInteger: Int)
  case class D(intField: Int, stringField: String)
  case class E(noFormat: ZonedDateTime)
  case object F

  case class DTO1(c: C, i: Int)
  case class DTO2(c: Option[C], i: Int)
  case class Compound(CField: C, DField: D)

  case class Parametrized1[T](field: T)
  case class Parametrized2[T0, T1](field1: T0, field2: T1)

  case class R(a: Int, rs: Seq[R])

  case class F1(f1: String) extends AnyVal

  case class ClassWith23Fields(
      f1: F1,
      f2: Int,
      f3: Long,
      f4: Option[String],
      f5: Option[String],
      fieldNumberSix: String,
      f7: List[String],
      f8: String,
      f9: String,
      f10: String,
      f11: String,
      f12: String,
      f13: String,
      f14: String,
      f15: String,
      f16: String,
      f17: String,
      f18: String,
      f19: String,
      f20: String,
      f21: String,
      f22: String,
      f23: Boolean
  )

  object ClassWith23Fields {
    val Example = ClassWith23Fields(
      f1 = F1("f1 value"),
      f2 = 2,
      f3 = 3L,
      f4 = None,
      f5 = Some("f5 value"),
      fieldNumberSix = "six",
      f7 = List("f7 value 1", "f7 value 2"),
      f8 = "f8 value",
      f9 = "f9 value",
      f10 = "f10 value",
      f11 = "f11 value",
      f12 = "f12 value",
      f13 = "f13 value",
      f14 = "f14 value",
      f15 = "f15 value",
      f16 = "f16 value",
      f17 = "f17 value",
      f18 = "f18 value",
      f19 = "f19 value",
      f20 = "f20 value",
      f21 = "f21 value",
      f22 = "f22 value",
      f23 = true
    )
  }

  case class ClassWith23FieldsNested(
      f1: F1,
      f2: ClassWith23Fields,
      f3: Long,
      f4: Option[String],
      f5: Option[String],
      fieldNumberSix: String,
      f7: List[String],
      f8: String,
      f9: String,
      f10: String,
      f11: String,
      f12: String,
      f13: String,
      f14: String,
      f15: String,
      f16: String,
      f17: String,
      f18: String,
      f19: String,
      f20: String,
      f21: String,
      f22: String,
      f23: Boolean
  )

  object ClassWith23FieldsNested {
    val Example: ClassWith23FieldsNested = ClassWith23FieldsNested(
      f1 = F1("f1 value"),
      f2 = ClassWith23Fields.Example,
      f3 = 3L,
      f4 = None,
      f5 = Some("f5 value"),
      fieldNumberSix = "six",
      f7 = List("f7 value 1", "f7 value 2"),
      f8 = "f8 value",
      f9 = "f9 value",
      f10 = "f10 value",
      f11 = "f11 value",
      f12 = "f12 value",
      f13 = "f13 value",
      f14 = "f14 value",
      f15 = "f15 value",
      f16 = "f16 value",
      f17 = "f17 value",
      f18 = "f18 value",
      f19 = "f19 value",
      f20 = "f20 value",
      f21 = "f21 value",
      f22 = "f22 value",
      f23 = true
    )
  }
}

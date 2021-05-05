package hstore

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import slick.lifted.Isomorphism

class MapIsomorphismTest extends AnyFunSuite with Matchers {
  import pl.iterators.kebs._

  case class Key(value: String)
  case class Value(value: Int)

  test("Case classes isomorphisms implies map isomorphism") {
    val iso = implicitly[Isomorphism[Map[Key, Value], Map[String, Int]]]
    iso.map(Map(Key("a") -> Value(0), Key("b") -> Value(1))) shouldBe Map("a" -> 0, "b"             -> 1)
    iso.comap(Map("a"    -> 0, "b"             -> 1)) shouldBe Map(Key("a")   -> Value(0), Key("b") -> Value(1))
  }
}

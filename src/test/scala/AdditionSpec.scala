import org.scalacheck._
import org.scalacheck.Prop.forAll

object AdditionSpecification extends Properties("Addition") {

  property("identity") = forAll { (x: Int) =>
    x + 0 == x
  }

  property("associativity") = forAll { (x: Int, y: Int, z: Int) =>
    (x + y) + z == x + (y + z)
  }

  property("commutativity") = forAll { (x: Int, y: Int) =>
    x + y == y + x
  }

  property("invertibility") = forAll { (x: Int) =>
    x + -x == 0
  }

  property("monotonicity") = forAll { (x: Int, y: Int, z: Int) =>
    if (x < y) {
      x + z < y + z
    } else true
  }

  property("distribution") = forAll { (x: Int, y: Int, z: Int) =>
    x * (y + z) == (x * y) + (x * z)
  }

  property("sign") = forAll { (x: Int, y: Int) =>
    if (x > 0 && y > 0) {
      x + y > 0
    } else true
  }
}

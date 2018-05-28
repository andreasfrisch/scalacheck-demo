import org.scalacheck._
import org.scalacheck.Prop.forAll

object DiamondKataSpec extends Properties("DiamondKata") {

  val diamondCharGen: Gen[Char] = Gen.choose('A', 'Z')

  // superfluous due to the below test
  property("odd line count") = forAll(diamondCharGen) { (c: Char) =>
    DiamondKata.diamond(c).size % 2 == 1
  }

  property("correct line count") = forAll(diamondCharGen) { (c: Char) =>
    DiamondKata.diamond(c).size == (c.toInt - 'A'.toInt) * 2 + 1
  }

  property("same line length throughout") = forAll(diamondCharGen) { (c: Char) =>
    val expectedLineLength = (c.toInt - 'A'.toInt) * 2 + 1
    DiamondKata.diamond(c).forall(line => line.size == expectedLineLength)
  }

  property("symmetry top to bottom") = forAll(diamondCharGen) { (c: Char) =>
    val diamond = DiamondKata.diamond(c)
    diamond.indices.forall(i => diamond(i) == diamond(diamond.size - 1 - i))
  }

  property("symmetry left to right") = forAll(diamondCharGen) { (c: Char) =>
    DiamondKata.diamond(c).forall(line => line == line.reverse)
  }

  property("character correctness") = forAll(diamondCharGen) { (c: Char) =>
    val chars = DiamondKata.diamond(c).map(_.replaceAllLiterally(" ", "").head)
    val halfChars = chars.slice(0, chars.size / 2 + 1)
    halfChars.indices.forall(i => 'A'.toInt+i == chars(i).toInt)
  }
}

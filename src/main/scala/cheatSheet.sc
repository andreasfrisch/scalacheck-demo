import org.scalacheck._
import org.scalacheck.Prop.forAll

/*
 * Properties
 */
val myStupidProp = forAll { (x: Int) => x == 2 }
myStupidProp.check()
val myNaiveProp = forAll { (x: Int) => x == x }
myNaiveProp.check()

// forcing generators
(forAll { (x: Int) => x > 0 }).check()
(forAll(Gen.posNum[Int]) { (x: Int) => x > 0 }).check()

/*
 * Generators
 * and Arbitraries to avoid implicit madness
 */

// simple
Gen.alphaNumStr.sample
//Gen.posNum.sample // fails
Gen.posNum[Int].sample
Gen.choose(1,100).sample
Gen.listOf(Gen.alphaChar)
Gen.listOfN(10, Gen.alphaChar).sample

// less simple, but still quite
Gen.posNum[Int].suchThat(_ > 10).sample
Gen.alphaNumStr.suchThat(_.startsWith("a")).sample
Gen.alphaNumStr.filter(_.startsWith("a")).sample // filter <-> suchThat

Gen.alphaNumStr.retryUntil(_.startsWith("a")).sample // avoid those Nones

Gen.frequency(
  (1,1),
  (6,2),
  (3,3)
).sample

// A useful utility to generate samples from a Gen
def sample[A](n: Int)(gen: Gen[A]): List[Option[A]] =
  List.fill(n)(gen.sample)

sample(10)(Gen.frequency(
  (1,1),
  (6,2),
  (3,3)
))

// custom generators
val stringStartsWithABC = for {
  start <- Gen.oneOf('a','b','c')
  tail <- Gen.alphaStr
} yield start + tail


case class MyDate(day: Int, month: Int, year: Int)
val badDateGenerator = for {
  day <- Gen.choose(1,31)
  month <- Gen.choose(1,12)
  year <- Gen.choose(0,1999) // Oh No, Y2K
} yield MyDate(day, month, year)
badDateGenerator.sample
forAll(badDateGenerator) { (d: MyDate) => d.year < 2000}

implicit lazy val myDateArb: Arbitrary[MyDate] =
  Arbitrary(badDateGenerator)
forAll { (d: MyDate) => d.year < 2000}

Gen.resultOf(MyDate.apply _).sample


// what are arbitraries you say?
val intGenerator = Arbitrary.arbInt.arbitrary
intGenerator.sample


final case class User(name: String, age: Int, email: String)
val emailGenerator = for {
  domain <- Gen.alphaNumStr
  host <- Gen.alphaNumStr
  user <- Gen.alphaLowerStr
} yield user + "@" + host + "." + domain
val userGen = for {
  n <- Gen.alphaLowerStr
  a <- Gen.choose(0, 150)
  e <- emailGenerator
} yield User(n, a, e)
def myUserGen: Gen[User] = {
  for {
    n <- Gen.alphaLowerStr
    a <- Gen.choose(0, 150)
    e <- emailGenerator
  } yield User(n, a, e)
}
myUserGen.sample


/*
 * Testing Addition:
 * Properties of addition includes
 * - identity
 * - associativity
 * - commutativity
 * - invertibility
 * - monotonicity
 * - distribution
 * - sign
 */

/*
 * DiamonKata
 * Properties of diamond includes
 * - right-left symmetry
 * - top-down symmetry
 * - known line count
 * - known line length
 *
 * How to limit diamond input? [A-Z]
 */
// print diamonds
DiamondKata.diamond('A').map(line => print(s"$line\n"))
DiamondKata.diamond('G').map(line => print(s"$line\n"))

/*
 * Oddities
 */

// Shrinking -- and how to avoid it
implicit def noShrink[A]: Shrink[A] = Shrink.shrinkAny

/*
 * Types vs Test
 */
def mustBeHenry(s: String): String = {
  if (!s.startsWith("henry")) {
    throw new RuntimeException("MUST BE HENRY!")
  }
  s
}

//test
val henryString = Gen.alphaNumStr.map(str => s"henry$str")
val henryProperty = forAll(henryString) { s => mustBeHenry(s) == s}
henryProperty.check()

// vs

//type
class HenryString(string: String) {
  override def toString(): String = {
    s"henry$string"
  }
}
def mustBeHenry2(s: HenryString): String = ???
// this was a silly verification function that is no longer needed

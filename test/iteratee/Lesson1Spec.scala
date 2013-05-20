package iteratee

import org.specs2.mutable.SpecificationWithJUnit
import play.api.libs.iteratee._
import scala.concurrent.{Await, Future}
import utils.IterateeLessonUtils
import scala.Some
import scala.Some


class Lesson1Spec extends SpecificationWithJUnit with IterateeLessonUtils {

  "Our custom iteratee" should {

     "count the number of elements received" in {
       def counterIteratee[E] : Iteratee[E, Int] = ???
       evaluateIteratee(Enumerator(2, 7, 9, 10), counterIteratee[Int]) must equalTo(4)
     }

    "take the last element received" in {
      def takeLast[E] : Iteratee[E, Option[E]] = ???
      evaluateIteratee(Enumerator(2, 7, 9, 10), takeLast[Int]) must equalTo(Some(10))
    }

    "take the last three elements received" in {
      def takeLastThree[E] : Iteratee[E, List[E]] = ???
      evaluateIteratee(Enumerator(1, 2, 7, 9, 10), takeLastThree[Int]) must contain(7,9,10).only
    }

    "take the last n elements received" in {
      def takeLastN[E](n:Int) : Iteratee[E, List[E]] = ???
      evaluateIteratee(Enumerator(1, 2, 7, 9, 10), takeLastN[Int](4)) must contain(2,7,9,10).only
    }

  }

}

class Lesson1WithFoldSpec extends SpecificationWithJUnit with IterateeLessonUtils{

  "Our custom iteratee with fold" should {

    "count the number of elements received" in {
      def counterIteratee[E]: Iteratee[E, E] = ???
      evaluateIteratee(Enumerator(2, 7, 9, 10), counterIteratee[Int]) must equalTo(4)
    }

    "take the last element received" in {
      def takeLast[E] : Iteratee[E, Option[E]] = ???
      evaluateIteratee(Enumerator(2, 7, 9, 10), takeLast[Int]) must equalTo(Some(10))
    }

    "take the last three elements received" in {
      def takeLastThree[E] : Iteratee[E, List[E]] = ???
      evaluateIteratee(Enumerator(1, 2, 7, 9, 10), takeLastThree[Int]) must contain(7,9,10).only
    }

    "take the last n elements received" in {
      def takeLastN[E](n:Int) : Iteratee[E, List[E]] = ???
      evaluateIteratee(Enumerator(1, 2, 7, 9, 10), takeLastN[Int](4)) must contain(2,7,9,10).only

    }

  }

}

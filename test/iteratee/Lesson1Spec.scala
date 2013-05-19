package iteratee

import org.specs2.mutable.SpecificationWithJUnit
import play.api.libs.iteratee.{Enumerator, Iteratee}
import scala.concurrent.{Await, Future}
import scala.concurrent.duration.DurationInt

class Lesson1Spec extends SpecificationWithJUnit {

  def evaluateIteratee[I, O](enumerator: Enumerator[I], iteratee: Iteratee[I, O]) : Future[O] = {
    Await.result(enumerator |>>> iteratee, DurationInt(5) seconds)
  }


  "Our custom iteratee" should {

     "count the number of elements received" in {
       def counterIteratee : Iteratee[Int, Int] = ???
       evaluateIteratee(Enumerator(2, 7, 9, 10), counterIteratee) must equalTo(4)
     }

    "take the last element received" in {
      def takeLast : Iteratee[Int, Int] = ???
      evaluateIteratee(Enumerator(2, 7, 9, 10), takeLast) must equalTo(Some(10))
    }

    "take the last three elements received" in {
      def takeLastThree : Iteratee[Int, Int] = ???
      evaluateIteratee(Enumerator(2, 7, 9, 10), takeLastThree) must equalTo(List(7,9,10))
    }

    "take the last n elements received" in {
      def takeLastN(n:Int) : Iteratee[Int, Int] = ???
      evaluateIteratee(Enumerator(1, 2, 7, 9, 10), takeLastN(4)) must equalTo(List(2,7,9,10))
      evaluateIteratee(Enumerator(1, 2, 7, 9, 10), takeLastN(3)) must equalTo(List(7,9,10))
    }

  }

  // Limitations of the iteratee:
  // 1. It only generates one result

  // Bad uses of the iteratees
  // 1. Concurrent channel and feed whatever comes (it keeps untreated inputs on memory, ergo its vulnerable to attacks)

}

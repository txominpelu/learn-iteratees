package iteratee

import org.specs2.mutable.SpecificationWithJUnit
import play.api.libs.iteratee.{Input, Enumerator, Iteratee, Done, Cont}
import scala.concurrent.{Await, Future}
import scala.concurrent.duration.DurationInt

trait IterateeLessonUtils {

  def evaluateIteratee[I, O](enumerator: Enumerator[I], iteratee: Iteratee[I, O]) : O = {
    Await.result(enumerator |>>> iteratee, DurationInt(5) seconds)
  }

}
class Lesson1Spec extends SpecificationWithJUnit with IterateeLessonUtils {

  "Our custom iteratee" should {

     "count the number of elements received" in {
       def counterIteratee[E] : Iteratee[E, Int] = {
         def step(count: Int)(input:Input[E]) : Iteratee[E, Int] = input match {
           case Input.El(_) => Cont(step(count + 1))
           case Input.EOF => Done(count)
         }
         Cont(step(0))
       }
       evaluateIteratee(Enumerator(2, 7, 9, 10), counterIteratee[Int]) must equalTo(4)
     }

    "take the last element received" in {
      def takeLast[E] : Iteratee[E, Option[E]] = {
        def step(last: Option[E])(input:Input[E]) : Iteratee[E, Option[E]] = input match {
          case Input.El(x) => Cont(step(Some(x)))
          case Input.EOF => Done(last)
        }
        Cont(step(None))
      }
      evaluateIteratee(Enumerator(2, 7, 9, 10), takeLast[Int]) must equalTo(Some(10))
    }

    "take the last three elements received" in {
      def takeLastThree[E] : Iteratee[E, List[E]] = {
        def step(lastThree: List[E])(input:Input[E]) : Iteratee[E, List[E]] = (input, lastThree) match {
          case (Input.El(elem), x :: y :: z) => {
            Cont(step(List(elem, x, y)))
          }
          case (Input.El(elem), list)  => Cont(step(elem :: list))
          case (Input.EOF,_) => Done(lastThree)
        }
        Cont(step(Nil))
      }
      evaluateIteratee(Enumerator(1, 2, 7, 9, 10), takeLastThree[Int]) must contain(7,9,10).only
    }

    "take the last n elements received" in {
      def takeLastN[E](n:Int) : Iteratee[E, List[E]] = {
        def step(lastThree: List[E])(input:Input[E]) : Iteratee[E, List[E]] = (input, lastThree) match {
          case (Input.El(elem), list) if list.length >= n => {
            Cont(step(elem :: list.take(n - 1)))
          }
          case (Input.El(elem), list)  => Cont(step(elem :: list))
          case (Input.EOF,_) => Done(lastThree)
        }
        Cont(step(Nil))
      }
      evaluateIteratee(Enumerator(1, 2, 7, 9, 10), takeLastN[Int](4)) must contain(2,7,9,10).only
    }

  }



  // Limitations of the iteratee:
  // 1. It only generates one result

  // Bad uses of the iteratees
  // 1. Concurrent channel and feed whatever comes (it keeps untreated inputs on memory, ergo its vulnerable to attacks)

}

class Lesson1WithFoldSpec extends SpecificationWithJUnit with IterateeLessonUtils{

  "Our custom iteratee with fold" should {

    "count the number of elements received" in {
      def counterIteratee[E] = Iteratee.fold(0)((count, input:E) => count + 1)
      evaluateIteratee(Enumerator(2, 7, 9, 10), counterIteratee[Int]) must equalTo(4)
    }

    "take the last element received" in {
      def takeLast[E] = Iteratee.fold[E,Option[E]](None)((last, input:E) => Some(input))
      evaluateIteratee(Enumerator(2, 7, 9, 10), takeLast[Int]) must equalTo(Some(10))
    }

    "take the last three elements received" in {
      def takeLastThree[E] = Iteratee.fold[E,List[E]](Nil)((lastThree, elem:E) => lastThree match {
        case x :: y :: z => List(elem, x, y)
        case list => elem :: list
      })
      evaluateIteratee(Enumerator(1, 2, 7, 9, 10), takeLastThree[Int]) must contain(7,9,10).only
    }

    "take the last n elements received" in {
      def takeLastN[E](n:Int) = Iteratee.fold[E,List[E]](Nil)((lastThree, elem:E) => lastThree match {
        case list if list.length >= n => {
          elem :: list.take(n - 1)
        }
        case list => elem :: list
      })
      evaluateIteratee(Enumerator(1, 2, 7, 9, 10), takeLastN[Int](4)) must contain(2,7,9,10).only

    }

  }

}

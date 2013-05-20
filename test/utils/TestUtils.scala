package utils

import play.api.libs.iteratee.{Iteratee, Enumerator}
import scala.concurrent.Await
import scala.concurrent.duration.DurationInt

trait IterateeLessonUtils {

  def evaluateIteratee[I, O](enumerator: Enumerator[I], iteratee: Iteratee[I, O]) : O = {
    Await.result(enumerator |>>> iteratee, DurationInt(5) seconds)
  }

}

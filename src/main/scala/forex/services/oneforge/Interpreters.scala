package forex.services.oneforge

import java.time.OffsetDateTime

import forex.domain._
import monix.eval.Task
import org.atnos.eff._
import org.atnos.eff.all._
import org.atnos.eff.addon.monix.task._
import org.atnos.eff.memo._memo

import scala.concurrent.Future

object Interpreters {
  def dummy[R](
      implicit
      m1: _task[R]
  ): Algebra[Eff[R, ?]] = new Dummy[R]

  def live[R](
      forexProxy: ForexCachedProxy
  )(
      implicit
      m1: _Task[R],
  ): Algebra[Eff[R, ?]] = new Live[R](forexProxy)
}

final class Dummy[R] private[oneforge] (
    implicit
    m1: _task[R]
) extends Algebra[Eff[R, ?]] {
  override def get(
      pair: Rate.Pair
  ): Eff[R, Error Either Rate] =
    for {
      result ← fromTask(Task.now(Rate(pair, Price(BigDecimal(100)), Timestamp.now)))
    } yield Right(result)
}

private[oneforge] final class Live[R](
    forexProxy: ForexCachedProxy
)(
    implicit
    m1: _Task[R]
) extends Algebra[Eff[R, ?]] {
  override def get(
      pair: Rate.Pair
  ): Eff[R, Error Either Rate] =
    for {
      result ← fromTask(Task.now(forexProxy.rate(pair)))
    } yield Right(result)
}

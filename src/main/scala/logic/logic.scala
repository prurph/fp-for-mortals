package logic

import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit

import scala.language.higherKinds
import scala.concurrent.duration._

import scalaz._
import Scalaz._

import algebra._

final case class WorldView(
    backlog: Int,
    agents: Int,
    managed: NonEmptyList[MachineNode],
    alive: Map[MachineNode, ZonedDateTime],
    pending: Map[MachineNode, ZonedDateTime], // requested at ZDT
    time: ZonedDateTime
)

final class DynAgents[F[_]](implicit M: Monad[F], d: Drone[F], m: Machines[F]) {
  def initial: F[WorldView] =
    for {
      db <- d.getBacklog
      da <- d.getAgents
      mm <- m.getManaged
      ma <- m.getAlive
      mt <- m.getTime
    } yield WorldView(db, da, mm, ma, Map.empty, mt)

  def update(old: WorldView): F[WorldView] =
    for {
      snap <- initial
      changed = symdiff(old.alive.keySet, snap.alive.keySet)
      pending = (old.pending -- changed).filterNot {
        case (_, started) => timediff(started, snap.time) >= 10.minutes
      }
      update = snap.copy(pending = pending)
    } yield update

  private def symdiff[T](a: Set[T], b: Set[T]) =
    (a union b) -- (a intersect b)

  private def timediff(from: ZonedDateTime, to: ZonedDateTime): FiniteDuration =
    ChronoUnit.MINUTES.between(from, to).minutes
}

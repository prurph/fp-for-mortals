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

final class DynAgents[F[_]](implicit M: Monad[F], d: Drone[F], m: Machines[F])

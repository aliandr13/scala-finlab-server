package com.github.aliandr13.finlab

import cats.effect._
import cats.implicits._
import com.github.aliandr13.finlab.domain.users.UserService
import com.github.aliandr13.finlab.infrastructure.endpoint.{TechEndpoints, UserEndPoints}
import com.github.aliandr13.finlab.infrastructure.repository.UserRepositoryInMemory
import doobie.util.ExecutionContexts
import org.http4s.implicits._
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.server.{Router, Server => H4Server}

/**
  * @author Alexander Zenkevich
  * @since 2019-03-22
  */
object Server extends IOApp {

  def createServer[F[_]: ContextShift: ConcurrentEffect: Timer]: Resource[F, H4Server[F]] =
    for {
      txnEc <- ExecutionContexts.cachedThreadPool[F]
      userRepo = UserRepositoryInMemory[F]()
      userService = UserService[F](userRepo)
      services = TechEndpoints.endpoints[F]() <+> UserEndPoints.endpoints[F](userService)
      httpApp = Router("/" -> services).orNotFound
      server <- BlazeServerBuilder[F]
        .bindHttp(9090, "localhost")
        .withHttpApp(httpApp)
        .resource
    } yield server

  override def run(args: List[String]): IO[ExitCode] =
    createServer.use(_ => IO.never).as(ExitCode.Success)
}

package com.github.aliandr13.finlab

import cats.effect._
import cats.implicits._
import com.github.aliandr13.finlab.domain.accounts.{AccountService, AccountValidationInterpreter}
import com.github.aliandr13.finlab.domain.transactions.TransactionService
import com.github.aliandr13.finlab.domain.users.UserService
import com.github.aliandr13.finlab.infrastructure.endpoint.{
  AccountEndpoints,
  TechEndpoints,
  TransactionEndpoints,
  UserEndPoints
}
import com.github.aliandr13.finlab.infrastructure.repository.{
  AccountRepositoryInMemory,
  TransactionRepositoryInMemory,
  UserRepositoryInMemory
}
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
      txnEc          <- ExecutionContexts.cachedThreadPool[F]
      userRepo       =  UserRepositoryInMemory[F]()
      trxRepo        =  TransactionRepositoryInMemory[F]()
      accRepo        =  AccountRepositoryInMemory[F]()
      userService    =  UserService[F](userRepo)
      accValidator   =  AccountValidationInterpreter[F](accRepo)
      accService     =  AccountService[F](accRepo, accValidator)
      trxService     =  TransactionService[F](trxRepo)
      services       =  TechEndpoints.endpoints[F]() <+>
        UserEndPoints.endpoints[F](userService) <+>
        AccountEndpoints.endpoints[F](accService) <+>
        TransactionEndpoints.endpoints[F](trxService)

      httpApp = Router("/" -> services).orNotFound
      server <- BlazeServerBuilder[F]
        .bindHttp(9090, "localhost")
        .withHttpApp(httpApp)
        .resource
    } yield server

  override def run(args: List[String]): IO[ExitCode] =
    createServer.use(_ => IO.never).as(ExitCode.Success)
}

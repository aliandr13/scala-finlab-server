package com.github.aliandr13.finlab.infrastructure.endpoint

import cats.effect.Effect
import cats.implicits._
import com.github.aliandr13.finlab.domain.accounts.{Account, AccountService}
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl
import org.http4s.{EntityDecoder, HttpRoutes}

/**
  * @author Alexander Zenkevich
  * @since 2019-05-04
  */
class AccountEndpoints[F[_]: Effect] extends Http4sDsl[F] {

  implicit val accountDecoder: EntityDecoder[F, Account] = jsonOf

  private val ACCOUNT = "account"

  private def accountEndpoint(): HttpRoutes[F] =
    HttpRoutes.of[F] {
      case GET -> Root / ACCOUNT => Ok("{\"name\": \"account endpoint\"}")
    }

  private def searchByUser(accountService: AccountService[F]): HttpRoutes[F] =
    HttpRoutes.of[F] {
      case GET -> Root / ACCOUNT / "user" / userId =>
        Ok(accountService.getAccountByUser(userId.toLong).asJson)
    }

  def endpoints(accountService: AccountService[F]): HttpRoutes[F] =
    accountEndpoint <+> searchByUser(accountService)
}

object AccountEndpoints {
  def endpoints[F[_]: Effect](accountService: AccountService[F]): HttpRoutes[F] =
    new AccountEndpoints[F].endpoints(accountService)
}

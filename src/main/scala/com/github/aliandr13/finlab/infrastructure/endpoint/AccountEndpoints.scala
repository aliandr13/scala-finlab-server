package com.github.aliandr13.finlab.infrastructure.endpoint

import cats.effect.Effect
import cats.implicits._
import com.github.aliandr13.finlab.domain.{AccountAlreadyExistsError, AccountNotFoundError}
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

  private val ACCOUNTS = "accounts"

  private def accountEndpoint(): HttpRoutes[F] =
    HttpRoutes.of[F] {
      case GET -> Root / ACCOUNTS => Ok("{\"name\": \"accounts endpoint\"}")
    }

  private def getAccountEndpoint(accountService: AccountService[F]): HttpRoutes[F] =
    HttpRoutes.of[F] {
      case GET -> Root / ACCOUNTS / LongVar(id) =>
        accountService.get(id).value.flatMap {
          case Right(found) => Ok(found.asJson)
          case Left(AccountNotFoundError) => NotFound(s"Account not found id=${id}")
        }
    }

  private def createAccountEndpoint(accountService: AccountService[F]): HttpRoutes[F] =
    HttpRoutes.of[F] {
      case req @ POST -> Root / ACCOUNTS =>
        val action = for {
          account <- req.as[Account]
          result <- accountService.creat(account).value
        } yield result

        action.flatMap {
          case Right(saved) => Ok(saved.asJson)
          case Left(AccountAlreadyExistsError(existing)) =>
            Conflict(s"The account ${existing.name} already exists")
        }
    }

  private def updateAccountEndpoint(accountService: AccountService[F]): HttpRoutes[F] =
    HttpRoutes.of[F] {
      case req @ PUT -> Root / ACCOUNTS =>
        val action = for {
          acc <- req.as[Account]
          result <- accountService.update(acc).value
        } yield result

        action.flatMap {
          case Right(updated) => Ok(updated.asJson)
          case Left(AccountNotFoundError) => NotFound("Account was not found")
        }
    }

  private def deleteAccountEndpoint(accountService: AccountService[F]): HttpRoutes[F] =
    HttpRoutes.of[F] {
      case DELETE -> Root / ACCOUNTS / LongVar(id) =>
        for {
          _ <- accountService.delete(id)
          resp <- Ok()
        } yield resp
    }

  private def searchByUser(accountService: AccountService[F]): HttpRoutes[F] =
    HttpRoutes.of[F] {
      case GET -> Root / ACCOUNTS / "user" / userId =>
        Ok(accountService.getAccountByUser(userId.toLong).asJson)
    }

  def endpoints(accService: AccountService[F]): HttpRoutes[F] =
    accountEndpoint <+>
      searchByUser(accService) <+>
      getAccountEndpoint(accService) <+>
      createAccountEndpoint(accService) <+>
      updateAccountEndpoint(accService) <+>
      deleteAccountEndpoint(accService)
}

object AccountEndpoints {
  def endpoints[F[_]: Effect](accountService: AccountService[F]): HttpRoutes[F] =
    new AccountEndpoints[F].endpoints(accountService)
}

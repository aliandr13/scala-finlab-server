package com.github.aliandr13.finlab.infrastructure.endpoint

import cats.effect.Effect
import cats.implicits._
import com.github.aliandr13.finlab.domain.UserNotFoundError
import com.github.aliandr13.finlab.domain.users.{User, UserService}
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl
import org.http4s.{EntityDecoder, HttpRoutes}

import scala.language.higherKinds

/**
  * @author Alexander Zenkevich
  * @since 2019-03-23
  */
class UserEndPoints[F[_]: Effect] extends Http4sDsl[F] {

  implicit val userDecoder: EntityDecoder[F, User] = jsonOf

  private val USER = "user"

  private def userEndpoint(): HttpRoutes[F] =
    HttpRoutes.of[F] {
      case GET -> Root / USER => Ok("{\"name\": \"user endpoint\"}")
    }

  private def searchByName(userService: UserService[F]): HttpRoutes[F] =
    HttpRoutes.of[F] {
      case GET -> Root / USER / userName =>
        userService.getUserByName(userName).value.flatMap {
          case Right(found) => Ok(found.asJson)
          case Left(UserNotFoundError) => NotFound(s"User not found: $userName")
        }
    }

  def endpoints(userService: UserService[F]): HttpRoutes[F] =
    userEndpoint() <+> searchByName(userService)
}

object UserEndPoints {
  def endpoints[F[_]: Effect](userService: UserService[F]): HttpRoutes[F] =
    new UserEndPoints[F].endpoints(userService)
}

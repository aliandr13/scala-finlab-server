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

  private val USERS = "users"

  /**
    * Get User info endpoint
    *
    * @return
    */
  private def userEndpoint(): HttpRoutes[F] =
    HttpRoutes.of[F] {
      case GET -> Root / USERS => Ok("{\"name\": \"user endpoint\"}")
    }

  /**
    * Get User by name
    * @param userService user Service
    * @return
    */
  private def searchByName(userService: UserService[F]): HttpRoutes[F] =
    HttpRoutes.of[F] {
      case GET -> Root / USERS / userName =>
        userService.getUserByName(userName).value.flatMap {
          case Right(found) => Ok(found.asJson)
          case Left(UserNotFoundError) => NotFound(s"User not found: $userName")
        }
    }

  /**
    * Create new User
    * @return
    */
  private def create(): HttpRoutes[F] =
    HttpRoutes.of[F] {
      case req @ POST -> Root / USERS =>
        val user = for {
          u <-req.as[User]
        } yield u
        System.out.println(user)
        Ok("{\"name\": \"user create endpoint\"}")
    }

  /**
    * Update user
    *
    * @return
    */
  private def update(): HttpRoutes[F] =
    HttpRoutes.of[F] {
      case req @ PUT -> Root / USERS =>
        val user = for {
          u <-req.as[User]
        } yield u
        System.out.println(user)
        Ok("{\"name\": \"user update endpoint\"}")
    }

  /**
    * Delete User by Id
    *
    * @return
    */
  private def delete(): HttpRoutes[F] =
    HttpRoutes.of[F] {
      case req @ DELETE -> Root / USERS =>
        val user = for {
          u <-req.as[User]
        } yield u
        System.out.println(user)
        Ok("{\"name\": \"user delete endpoint\"}")
    }

  def endpoints(userService: UserService[F]): HttpRoutes[F] =
    userEndpoint() <+> searchByName(userService) <+> create() <+> update() <+> delete()
}

object UserEndPoints {
  def endpoints[F[_]: Effect](userService: UserService[F]): HttpRoutes[F] =
    new UserEndPoints[F].endpoints(userService)
}

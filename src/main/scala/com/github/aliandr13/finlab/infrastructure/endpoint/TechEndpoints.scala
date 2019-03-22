package com.github.aliandr13.finlab.infrastructure.endpoint

import cats.effect.Effect
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl

/**
  * @author Alexander Zenkevich
  * @since 2019-03-22
  */
class TechEndpoints[F[_] : Effect] extends Http4sDsl[F] {

  def ping: HttpRoutes[F] =
    HttpRoutes.of[F] {
      case GET -> Root / "ping" => Ok("Pong!\n")
    }

  def endpoints: HttpRoutes[F] = ping
}

object TechEndpoints {
  def endpoints[F[_] : Effect](): HttpRoutes[F] =
    new TechEndpoints[F].endpoints
}
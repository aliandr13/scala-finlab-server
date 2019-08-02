package com.github.aliandr13.finlab.infrastructure.endpoint

import java.util.UUID

import cats.effect.Effect
import cats.implicits._
import com.github.aliandr13.finlab.domain.transactions.{Transaction, TransactionService}
import io.circe.generic.auto._
import org.http4s.circe.jsonOf
import org.http4s.dsl.Http4sDsl
import org.http4s.{EntityDecoder, HttpRoutes}

/**
  * @author Alexander Zenkevich
  * @since 2019-05-10
  */
class TransactionEndpoints[F[_]: Effect] extends Http4sDsl[F] {

  implicit val transactionDecoder: EntityDecoder[F, Transaction] = jsonOf

  private val TRANSACTION = "transaction"

  private def transactionEndpoint(): HttpRoutes[F] =
    HttpRoutes.of[F] {
      case GET -> Root / TRANSACTION => Ok("{\"name\": \"transaction endpoint\"}")
    }

  private def getById(transactionService: TransactionService[F]): HttpRoutes[F] =
    HttpRoutes.of[F] {
      case GET -> Root / TRANSACTION / transactionId => {
          transactionService.get(UUID.randomUUID())
        Ok(s"transaction get by id endpoint $transactionId")
      }
    }

  def endpoints(transactionService: TransactionService[F]): HttpRoutes[F] =
    transactionEndpoint <+> getById(transactionService)
}

object TransactionEndpoints {
  def endpoints[F[_]: Effect](trxService: TransactionService[F]): HttpRoutes[F] =
    new TransactionEndpoints[F].endpoints(trxService)
}

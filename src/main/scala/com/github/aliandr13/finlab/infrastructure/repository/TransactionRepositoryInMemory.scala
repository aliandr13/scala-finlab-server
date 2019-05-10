package com.github.aliandr13.finlab.infrastructure.repository

import java.util.UUID

import cats.Applicative
import cats.implicits._
import com.github.aliandr13.finlab.domain.transactions.{Transaction, TransactionRepositoryAlgebra}

import scala.collection.concurrent.TrieMap

/**
  * @author Alexander Zenkevich
  * @since 2019-05-10
  */
class TransactionRepositoryInMemory[F[_]: Applicative] extends TransactionRepositoryAlgebra[F] {

  private val cache = new TrieMap[UUID, Transaction]()

  override def create(trx: Transaction): F[Transaction] = {
    val uuid = UUID.randomUUID()
    val toSave = trx.copy(Some(uuid))
    cache += (uuid -> toSave)
    toSave.pure[F]
  }

  override def read(id: UUID): F[Option[Transaction]] =
    cache.get(id).pure[F]

  override def update(trx: Transaction): F[Option[Transaction]] = trx.id.traverse { id =>
    cache.update(id, trx)
    trx.pure[F]
  }

  override def delete(trx: Transaction): F[Option[Transaction]] = trx.id.traverse { id =>
    cache.remove(id)
    trx.pure[F]
  }
}

object TransactionRepositoryInMemory {
  def apply[F[_]: Applicative]() = new TransactionRepositoryInMemory[F]
}

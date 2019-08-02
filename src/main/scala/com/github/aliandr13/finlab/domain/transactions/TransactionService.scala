package com.github.aliandr13.finlab.domain.transactions

import java.util.UUID

import cats.Monad

/**
  * @author Alexander Zenkevich
  * @since 2019-05-10
  */
class TransactionService[F[_]](repository: TransactionRepositoryAlgebra[F]) {

  def get(id: UUID): F[Option[Transaction]] =
    repository.read(id)

  def add(trx: Transaction): F[Transaction] =
    repository.create(trx)
}

object TransactionService {
  def apply[F[_]: Monad](repository: TransactionRepositoryAlgebra[F]) =
    new TransactionService(repository)
}

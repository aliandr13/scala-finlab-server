package com.github.aliandr13.finlab.domain.transactions

import java.util.UUID


/**
  * @author Alexander Zenkevich
  * @since 2019-05-10
  */
trait TransactionRepositoryAlgebra[F[_]] {

  // CRUD
  def create(trx: Transaction): F[Transaction]

  def read(id: UUID) : F[Option[Transaction]]

  def update(trx: Transaction): F[Option[Transaction]]

  def delete(trx: Transaction): F[Option[Transaction]]

}

package com.github.aliandr13.finlab.domain.accounts

import java.time.LocalDateTime

/**
  * @author Alexander Zenkevich
  * @since 2019-04-30
  */
trait AccountRepositoryAlgebra[F[_]] {

  def create(account: Account): F[Account]

  def update(account: Account): F[Option[Account]]

  def get(accountId: Long): F[Option[Account]]

  def delete(accountId: Long): F[Option[Account]]

  def findByDate(from: LocalDateTime, to: LocalDateTime): F[List[Account]]

  def findByUser(userId: Long): F[List[Account]]

  def list(pageSize: Int, offset: Int): F[List[Account]]

}

package com.github.aliandr13.finlab.domain.accounts

/**
  * @author Alexander Zenkevich
  * @since 2019-04-30
  */
trait AccountRepositoryAlgebra[F[_]] {

  def create(account: Account): F[Account]

  def update(account: Account): F[Option[Account]]

  def get(accountId: Long): F[Option[Account]]

  def delete(accountId: Long): F[Option[Account]]

  def findByName(name: String): F[Option[Account]]

  def findByUser(userId: Long): F[List[Account]]

  def list(pageSize: Int, offset: Int): F[List[Account]]

}

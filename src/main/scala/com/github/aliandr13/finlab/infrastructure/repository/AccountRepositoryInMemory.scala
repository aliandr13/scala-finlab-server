package com.github.aliandr13.finlab.infrastructure.repository

import java.util.Random

import cats.Applicative
import cats.implicits._
import com.github.aliandr13.finlab.domain.accounts.{Account, AccountRepositoryAlgebra}

import scala.collection.concurrent.TrieMap

/**
  * @author Alexander Zenkevich
  * @since 2019-05-04
  */
class AccountRepositoryInMemory[F[_]: Applicative] extends AccountRepositoryAlgebra[F] {

  private val cache = new TrieMap[Long, Account]
  private val rnd = new Random()

  override def create(account: Account): F[Account] = {
    val id = rnd.nextLong
    val toSave = account.copy(id.some)
    cache += (id -> toSave)
    toSave.pure[F]
  }

  override def update(account: Account): F[Option[Account]] = account.accountId.traverse { id =>
    cache.update(id, account)
    account.pure[F]
  }

  override def get(accountId: Long): F[Option[Account]] =
    cache.get(accountId).pure[F]

  override def delete(accountId: Long): F[Option[Account]] =
    cache.remove(accountId).pure[F]

  override def findByName(name: String): F[Option[Account]] =
    cache.values.toList
      .find(a => a.name == name)
      .pure[F]

  override def list(pageSize: Int, offset: Int): F[List[Account]] =
    cache.values.toList.slice(offset, pageSize + offset).pure[F]

  override def findByUser(userId: Long): F[List[Account]] =
    cache.values.toList.filter(_.userId == userId).pure[F]
}

object AccountRepositoryInMemory {
  def apply[F[_]: Applicative]() = new AccountRepositoryInMemory[F]
}

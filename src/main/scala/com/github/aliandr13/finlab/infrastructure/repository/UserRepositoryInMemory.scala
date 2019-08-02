package com.github.aliandr13.finlab.infrastructure.repository

import java.util.Random

import cats.Applicative
import cats.implicits._
import com.github.aliandr13.finlab.domain.users.{User, UserRepositoryAlgebra}

import scala.collection.concurrent.TrieMap

/**
  * @author Alexander Zenkevich
  * @since 2019-03-25
  */
class UserRepositoryInMemory[F[_]: Applicative] extends UserRepositoryAlgebra[F] {

  private val cache = new TrieMap[Long, User]
  private val rnd = new Random()

  override def create(user: User): F[User] = {
    val id = rnd.nextLong()
    val toSave = user.copy(id = id.some)
    cache += (id -> toSave)
    toSave.pure[F]
  }
  override def update(user: User): F[Option[User]] = user.id.traverse { id =>
    cache.update(id, user)
    user.pure[F]
  }

  override def get(userId: Long): F[Option[User]] = cache.get(userId).pure[F]

  override def delete(userId: Long): F[Option[User]] = cache.remove(userId).pure[F]

  override def findByUserName(userName: String): F[Option[User]] =
    cache.values.find(_.userName == userName).pure[F]

  override def deleteByUserName(userName: String): F[Option[User]] = {
    val result = for {
      user <- cache.values.find(_.userName == userName)
      deleted <- cache.remove(user.id.get)
    } yield deleted
    result.pure[F]
  }

  override def list(pageSize: Int, offset: Int): F[List[User]] =
    cache.values.toList.sortBy(_.lastName).slice(offset, offset + pageSize).pure[F]
}

object UserRepositoryInMemory {
  def apply[F[_]: Applicative]() = {
    val repo = new UserRepositoryInMemory[F]
    repo.create(User("jwick200", "John", "Wick", "wheresmycar@gmail.com", "wickofyourwit", "215-789-0123"))
    repo
  }
}

package com.github.aliandr13.finlab.domain.accounts

import cats._
import cats.data._
import com.github.aliandr13.finlab.domain.{AccountAlreadyExistsError, AccountNotFoundError}

/**
  * The entry point to account domain, works with repositories and validation to implement behaviour
  *
  * @param repository where we get data
  * @param validation provides validations to the service
  * @author Alexander Zenkevich
  * @since 2019-05-04
  */
class AccountService[F[_]](
    repository: AccountRepositoryAlgebra[F],
    validation: AccountValidationAlgebra[F]) {

  import cats.syntax.all._

  def getAccountByUser(userId: Long): List[Account] =
    if (userId > 0) List.empty
    else List.empty

  def get(id: Long)(implicit M: Monad[F]): EitherT[F, AccountNotFoundError.type, Account] =
    EitherT.fromOptionF(repository.get(id), AccountNotFoundError)

  def creat(account: Account)(
      implicit M: Monad[F]): EitherT[F, AccountAlreadyExistsError, Account] =
    for {
      _ <- validation.doesNotExists(account)
      saved <- EitherT.liftF(repository.create(account))
    } yield saved

  def update(account: Account)(
      implicit M: Monad[F]): EitherT[F, AccountNotFoundError.type, Account] =
    for {
      _ <- validation.exists(account.accountId)
      updated <- EitherT.fromOptionF(repository.update(account), AccountNotFoundError)
    } yield updated

  def delete(id: Long)(implicit M: Monad[F]): F[Unit] =
    repository.delete(id).as(())
}

object AccountService {
  def apply[F[_]: Monad](
      repository: AccountRepositoryAlgebra[F],
      validation: AccountValidationAlgebra[F]) = new AccountService(repository, validation)
}

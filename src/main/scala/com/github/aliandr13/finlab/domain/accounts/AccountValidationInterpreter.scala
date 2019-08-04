package com.github.aliandr13.finlab.domain
package accounts

import cats._
import cats.data.EitherT
import cats.implicits._

class AccountValidationInterpreter[F[_]: Monad](repository: AccountRepositoryAlgebra[F])
    extends AccountValidationAlgebra[F] {
  /**
   * Fails with a PetAlreadyExistsError
   *
   * @param account
   * @return
   */
  override def doesNotExists(account: Account): EitherT[F, AccountAlreadyExistsError, Unit] = EitherT{
    repository.fi
  }

  override def exists(accountId: Option[Long]): EitherT[F, AccountNotFoundError.type, Unit] = ???
}

object AccountValidationInterpreter {
  def apply(repository: AccountRepositoryAlgebra[F]): AccountValidationInterpreter = new AccountValidationInterpreter(repository)
}

package com.github.aliandr13.finlab.domain
package accounts

import cats._
import cats.data.EitherT
import cats.implicits._

class AccountValidationInterpreter[F[_]: Applicative](repository: AccountRepositoryAlgebra[F])
    extends AccountValidationAlgebra[F] {

  /**
    * Fails with a PetAlreadyExistsError
    *
    * @param account
    * @return
    */
  override def doesNotExists(account: Account): EitherT[F, AccountAlreadyExistsError, Unit] =
    EitherT {
      repository.findByName(account.name).map { matches =>
        if (matches != null) { Right(()) } else {
          Left(AccountAlreadyExistsError(account))
        }
      }
    }

  override def exists(accountId: Option[Long]): EitherT[F, AccountNotFoundError.type, Unit] =
    EitherT {
      accountId match {
        case Some(id) =>
          repository.get(id).map {
            case Some(_) => Right(())
            case _ => Left(AccountNotFoundError)
          }
        case _ => Either.left[AccountNotFoundError.type, Unit](AccountNotFoundError).pure[F]
      }
    }
}

object AccountValidationInterpreter {
  def apply[F[_]: Applicative](repository: AccountRepositoryAlgebra[F]) =
    new AccountValidationInterpreter(repository)
}

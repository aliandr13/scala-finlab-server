package com.github.aliandr13.finlab.domain.accounts

import scala.language.higherKinds
import cats.data.EitherT
import com.github.aliandr13.finlab.domain.AccountAlreadyExistsError
import com.github.aliandr13.finlab.domain.AccountNotFoundError

trait AccountValidationAlgebra[F[_]] {

  /**
    *  Fails with a PetAlreadyExistsError
    * @param account
    * @return
    */
  def doesNotExists(account: Account): EitherT[F, AccountAlreadyExistsError, Unit]

  def exists(accountId: Option[Long]): EitherT[F, AccountNotFoundError.type, Unit]
}

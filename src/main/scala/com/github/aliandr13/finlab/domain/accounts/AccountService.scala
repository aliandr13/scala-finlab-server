package com.github.aliandr13.finlab.domain.accounts

import cats.Monad

/**
  * @author Alexander Zenkevich
  * @since 2019-05-04
  */
class AccountService[F[_]: Monad] {

  def getAccountByUser(userId: Long): List[Account] =
    if (userId > 0) List.empty
    else List.empty

}

object AccountService {
  def apply[F[_]: Monad]: AccountService[F] = new AccountService()
}

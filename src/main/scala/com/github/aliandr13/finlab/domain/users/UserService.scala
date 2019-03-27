package com.github.aliandr13.finlab.domain.users

import cats._
import cats.data._
import com.github.aliandr13.finlab.domain.UserNotFoundError

/**
  * @author Alexander Zenkevich
  * @since 2019-03-23
  */
class UserService[F[_]: Monad](userRepo: UserRepositoryAlgebra[F]) {

  def getUserByName(userName: String): EitherT[F, UserNotFoundError.type, User] =
    EitherT.fromOptionF(userRepo.findByUserName(userName), UserNotFoundError)

}

object UserService {
  def apply[F[_]: Monad](userRepo: UserRepositoryAlgebra[F]): UserService[F] =
    new UserService(userRepo)
}

package com.github.aliandr13.finlab.domain
import com.github.aliandr13.finlab.domain.accounts.Account
import com.github.aliandr13.finlab.domain.users.User

/**
  * @author Alexander Zenkevich
  * @since 2019-03-23
  */
sealed trait ValidationError extends Product with Serializable
// User error
case object UserNotFoundError extends ValidationError
case class UserAlreadyExistsError(user: User) extends ValidationError
// Account error
case class AccountAlreadyExistsError(account: Account) extends ValidationError
case object AccountNotFoundError extends ValidationError

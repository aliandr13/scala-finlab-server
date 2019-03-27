package com.github.aliandr13.finlab.domain.users

/**
  * @author Alexander Zenkevich
  * @since 2019-03-23
  */
case class User(
    userName: String,
    firstName: String,
    lastName: String,
    email: String,
    hash: String,
    phone: String,
    id: Option[Long] = None
)

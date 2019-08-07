package com.github.aliandr13.finlab.domain.accounts

import java.math.BigDecimal

/**
  * @author Alexander Zenkevich
  * @since 2019-04-29
  */
case class Account(
    accountId: Option[Long],
    userId: Long,
    name: String,
    description: String,
    accountTypeId: Int,
    currencyId: Long,
    balance: BigDecimal,
)

package com.github.aliandr13.finlab.domain.transactions

import java.util.UUID

import com.github.aliandr13.finlab.domain.accounts.Account

/**
  * @author Alexander Zenkevich
  * @since 2019-04-30
  */
case class Transaction(
    id: Option[UUID],
    from: Account,
    to: Account,
    value: BigDecimal,
    description: String
)

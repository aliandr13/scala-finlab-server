package com.github.aliandr13.finlab.domain.accounts

import java.math.BigDecimal
import java.time.LocalDateTime

import com.github.aliandr13.finlab.domain.currencies.Currency

/**
  * @author Alexander Zenkevich
  * @since 2019-04-29
  */
case class Account(
                    accountId: Option[Long],
                    userId: Long,
                    name: String,
                    description: String,
                    accountType: AccountType,
                    currency: Currency,
                    balance: BigDecimal,
                    createdAt: LocalDateTime,
                    updatedAt: LocalDateTime
)

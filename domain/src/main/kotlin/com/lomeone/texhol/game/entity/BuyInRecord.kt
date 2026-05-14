package com.lomeone.texhol.game.entity

import jakarta.persistence.Embeddable
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import java.time.LocalDateTime

@Embeddable
class BuyInRecord(
    @Enumerated(EnumType.STRING)
    val type: BuyInType,

    @Enumerated(EnumType.STRING)
    val paymentMethod: PaymentMethod,

    val createdAt: LocalDateTime = LocalDateTime.now()
)

enum class BuyInType { INITIAL, RE_BUY }

enum class PaymentMethod { CASH, CARD, POINTS }

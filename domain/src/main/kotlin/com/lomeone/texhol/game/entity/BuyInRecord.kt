package com.lomeone.texhol.game.entity

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import java.time.LocalDateTime

@Embeddable
class BuyInRecord(
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val type: BuyInType,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val paymentMethod: PaymentMethod,

    @Column(nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()
)

enum class BuyInType { INITIAL, RE_BUY }

enum class PaymentMethod { CASH, CARD, POINTS }

package com.lomeone.texhol.entity

import com.lomeone.common.entity.AuditEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "buy_in_records")
class BuyInRecord(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_entry_id")
    val gameEntry: GameEntry,

    @Enumerated(EnumType.STRING)
    val type: BuyInType,

    @Enumerated(EnumType.STRING)
    val paymentMethod: PaymentMethod
) : AuditEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "buy_in_record_id")
    val id: Long = 0L
}

enum class BuyInType { INITIAL, RE_BUY }

enum class PaymentMethod { CASH, CREDIT_CARD, POINT }

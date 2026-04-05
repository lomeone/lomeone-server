package com.lomeone.texhol.entity

import com.lomeone.common.entity.AuditEntity
import com.lomeone.texhol.exception.GameEntryNotAliveException
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.Table

@Entity
@Table(name = "game_entries", indexes = [
    Index(name = "idx_game_entries_game_id_player_id_u1", columnList = "game_id, player_id", unique = true)
])
class GameEntry protected constructor(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id")
    val game: Game,
    player: Player,
) : AuditEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "game_entry_id")
    val id: Long = 0L

    @Enumerated(EnumType.STRING)
    var status: GameEntryStatus = GameEntryStatus.ALIVE
        protected set

    @ManyToOne
    @JoinColumn(name = "player_id")
    var player: Player = player
        protected set

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_entry_id")
    private val _buyInRecords: MutableList<BuyInRecord> = mutableListOf()
    val buyInRecords: List<BuyInRecord> get() = _buyInRecords.toList()

    val reBuyCount: Int get() = _buyInRecords.count { it.type == BuyInType.RE_BUY }

    companion object {
        fun create(
            game: Game,
            player: Player,
            initialPayment: PaymentMethod,
        ): GameEntry {
            val entry = GameEntry(game, player)
            entry._buyInRecords.add(BuyInRecord(entry, BuyInType.INITIAL, initialPayment))

            return entry
        }
    }

    fun changePlayer(player: Player) {
        this.player = player
    }

    fun addReBuy(method: PaymentMethod) {
        if (this.status != GameEntryStatus.ALIVE) { throw GameEntryNotAliveException(detail = mapOf("game" to this.game, "player" to this.player)) }
    }

    fun sitOut() {
        this.status = GameEntryStatus.SIT_OUT
    }
    fun returnToGame() {
        this.status = GameEntryStatus.ALIVE
    }
}

enum class GameEntryStatus { ALIVE, SIT_OUT }



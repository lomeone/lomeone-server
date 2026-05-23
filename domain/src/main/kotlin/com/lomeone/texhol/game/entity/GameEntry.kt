package com.lomeone.texhol.game.entity

import com.lomeone.common.entity.AuditEntity
import com.lomeone.texhol.player.entity.Player
import com.lomeone.texhol.game.exception.GameEntryNotAliveException
import jakarta.persistence.CollectionTable
import jakarta.persistence.Column
import jakarta.persistence.ElementCollection
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
import jakarta.persistence.Table

@Entity
@Table(
    name = "game_entries",
    indexes = [
        Index(
            name = "idx_game_entries_game_session_id_player_id_u1",
            columnList = "game_session_id, player_id",
            unique = true
        ),
        Index(
            name = "idx_game_entries_player_id",
            columnList = "player_id"
        )
    ]
)
class GameEntry protected constructor(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_session_id", nullable = false)
    val gameSession: GameSession,

    player: Player
) : AuditEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "game_entry_id")
    val id: Long = 0L

    @Enumerated(EnumType.STRING)
    var status: GameEntryStatus = GameEntryStatus.ALIVE
        protected set

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id")
    var player: Player = player
        protected set

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
        name = "buy_in_records",
        joinColumns = [JoinColumn(name = "game_entry_id")]
    )
    private val _buyInRecords: MutableList<BuyInRecord> = mutableListOf()
    val buyInRecords: List<BuyInRecord> get() = _buyInRecords.toList()

    val reBuyCount: Int get() = _buyInRecords.count { it.type == BuyInType.RE_BUY }

    companion object {
        fun create(
            gameSession: GameSession,
            player: Player,
            initialPayment: PaymentMethod
        ): GameEntry {
            val entry = GameEntry(gameSession, player)
            entry._buyInRecords.add(BuyInRecord(BuyInType.INITIAL, initialPayment))
            return entry
        }
    }

    fun changePlayer(player: Player) {
        this.player = player
    }

    fun addReBuy(method: PaymentMethod) {
        if (this.status != GameEntryStatus.ALIVE) {
            throw GameEntryNotAliveException(detail = mapOf("gameSession" to this.gameSession.id, "player" to this.player.id))
        }
        _buyInRecords.add(BuyInRecord(BuyInType.RE_BUY, method))
    }

    fun sitOut() {
        this.status = GameEntryStatus.SIT_OUT
    }

    fun returnToGame() {
        this.status = GameEntryStatus.ALIVE
    }
}

enum class GameEntryStatus { ALIVE, SIT_OUT }

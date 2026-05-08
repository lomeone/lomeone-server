package com.lomeone.texhol.game.entity

import com.lomeone.common.entity.AuditEntity
import com.lomeone.texhol.store.entity.Store
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
import jakarta.persistence.Table

@Entity
@Table(
    name = "game_sessions",
    indexes = [
        Index(
            name = "idx_game_sessions_store_id_game_type_id_session_u1",
            columnList = "store_id, game_type_id, session",
            unique = true
        ),
        Index(
            name = "idx_game_sessions_store_id_status",
            columnList = "store_id, status"
        ),
        Index(
            name = "idx_game_sessions_game_type_id",
            columnList = "game_type_id"
        )
    ]
)
class GameSession private constructor(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    val store: Store,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_type_id", nullable = false)
    val gameType: GameType,

    @Column(nullable = false)
    val session: Int
) : AuditEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "game_session_id")
    val id: Long = 0L

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: GameSessionStatus = GameSessionStatus.RECRUITING
        protected set

    companion object {
        fun create(
            store: Store,
            gameType: GameType,
            session: Int
        ): GameSession {
            require(gameType.belongsTo(store)) {
                "GameType does not belong to the given Store"
            }
            return GameSession(store, gameType, session)
        }
    }

    fun close() {
        this.status = GameSessionStatus.CLOSED
    }

    fun earlyClose() {
        this.status = GameSessionStatus.EARLY_CLOSE
    }

    fun reOpen() {
        this.status = GameSessionStatus.RECRUITING
    }
}

enum class GameSessionStatus {
    RECRUITING,   // 예약 접수 중
    EARLY_CLOSE,  // 조기 마감
    CLOSED        // 종료
}

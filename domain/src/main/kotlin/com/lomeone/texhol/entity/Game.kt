package com.lomeone.texhol.entity

import com.lomeone.common.entity.AuditEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "games", indexes = [
    Index(name = "idx_games_store_id_game_type_session_u1", columnList = "store_id, game_type, session", unique = true),
])
class Game(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "game_id")
    val id: Long = 0L,
    @ManyToOne
    @JoinColumn(name = "store_id")
    val store: Store,
    val gameType: String,
    val session: Int,
) : AuditEntity() {
    @Enumerated(EnumType.STRING)
    var status: GameStatus = GameStatus.RECRUITING
        protected set

    fun close() {
        this.status = GameStatus.CLOSED
    }

    fun reOpen() {
        this.status = GameStatus.RECRUITING
    }
}

enum class GameStatus {
    RECRUITING,
    CLOSED
}

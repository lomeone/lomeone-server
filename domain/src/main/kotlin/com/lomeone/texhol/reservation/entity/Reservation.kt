package com.lomeone.texhol.reservation.entity

import com.lomeone.common.entity.AuditEntity
import com.lomeone.texhol.player.entity.Player
import com.lomeone.texhol.game.entity.GameSession
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
    name = "reservations",
    indexes = [
        Index(
            name = "idx_reservations_game_session_id_player_id_u1",
            columnList = "game_session_id, player_id",
            unique = true
        ),
        Index(
            name = "idx_reservations_player_id",
            columnList = "player_id"
        )
    ]
)
class Reservation(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_session_id", nullable = false)
    val gameSession: GameSession,

    player: Player,

    @Column(nullable = false, length = 50)
    val time: String
) : AuditEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    val id: Long = 0L

    @ManyToOne
    @JoinColumn(name = "player_id")
    var player: Player = player
        protected set

    @Enumerated(EnumType.STRING)
    var status: ReservationStatus = ReservationStatus.WAITING
        protected set

    fun changePlayer(player: Player) {
        this.player = player
    }

    fun register() {
        this.status = ReservationStatus.REGISTERED
    }

    fun cancel() {
        this.status = ReservationStatus.CANCELLED
    }
}

enum class ReservationStatus { WAITING, REGISTERED, CANCELLED }

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
@Table(name = "reservations", indexes = [
    Index(name = "idx_reservations_game_id_player_id_u1", columnList = "game_id, player_id", unique = true)
])
class Reservation(
    @ManyToOne
    @JoinColumn(name = "game_id")
    val game: Game,
    player: Player,
    val time: String,
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

package com.lomeone.texhol.entity

import com.lomeone.common.entity.AuditEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.Table

@Entity
@Table(name = "players", indexes = [
    Index(name = "idx_players_nickname_u1", columnList = "nickname", unique = true)
])
class Player(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "player_id")
    val id: Long = 0L,
    val nickname: String,
) : AuditEntity()

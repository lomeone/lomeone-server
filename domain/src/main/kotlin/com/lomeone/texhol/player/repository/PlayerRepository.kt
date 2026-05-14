package com.lomeone.texhol.player.repository

import com.lomeone.texhol.player.entity.Player
import org.springframework.data.jpa.repository.JpaRepository

interface PlayerRepository : JpaRepository<Player, Long> {
    fun findByNickname(nickname: String): Player?
}

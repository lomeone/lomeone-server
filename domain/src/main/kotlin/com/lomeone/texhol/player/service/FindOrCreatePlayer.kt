package com.lomeone.texhol.player.service

import com.lomeone.texhol.player.entity.Player
import com.lomeone.texhol.player.repository.PlayerRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class FindOrCreatePlayer(
    private val playerRepository: PlayerRepository
) {
    @Transactional
    operator fun invoke(command: FindOrCreatePlayerCommand): Player {
        return playerRepository.findByNickname(command.nickname)
            ?: createNewPlayer(command.nickname)
    }

    private fun createNewPlayer(nickname: String): Player {
        val player = Player(nickname = nickname)
        return playerRepository.save(player)
    }
}

data class FindOrCreatePlayerCommand(
    val nickname: String
)

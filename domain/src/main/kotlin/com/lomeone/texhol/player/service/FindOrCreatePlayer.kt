package com.lomeone.texhol.player.service

import com.lomeone.texhol.player.entity.Player
import com.lomeone.texhol.player.repository.PlayerRepository
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service
import com.lomeone.texhol.common.TexholTransactional

@Service
class FindOrCreatePlayer(
    private val playerRepository: PlayerRepository
) {
    private val logger = KotlinLogging.logger {}

    @TexholTransactional
    operator fun invoke(command: FindOrCreatePlayerCommand): Player {
        logger.info { "Finding or creating player: nickname=${command.nickname}" }
        return playerRepository.findByNickname(command.nickname)
            ?: createNewPlayer(command.nickname)
    }

    private fun createNewPlayer(nickname: String): Player {
        val player = Player(nickname = nickname)
        val saved = playerRepository.save(player)
        logger.info { "Player created: id=${saved.id}, nickname=$nickname" }
        return saved
    }
}

data class FindOrCreatePlayerCommand(
    val nickname: String
)

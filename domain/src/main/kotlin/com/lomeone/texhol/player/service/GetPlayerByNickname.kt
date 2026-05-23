package com.lomeone.texhol.player.service

import com.lomeone.texhol.player.entity.Player
import com.lomeone.texhol.player.exception.PlayerNotFoundException
import com.lomeone.texhol.player.repository.PlayerRepository
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service
import com.lomeone.texhol.common.TexholTransactional

@Service
class GetPlayerByNickname(
    private val playerRepository: PlayerRepository
) {
    private val logger = KotlinLogging.logger {}

    @TexholTransactional(readOnly = true)
    operator fun invoke(command: GetPlayerByNicknameCommand): Player {
        logger.info { "Getting player: nickname=${command.nickname}" }
        return playerRepository.findByNickname(command.nickname)
            ?: throw PlayerNotFoundException(detail = mapOf("nickname" to command.nickname))
    }
}

data class GetPlayerByNicknameCommand(
    val nickname: String
)

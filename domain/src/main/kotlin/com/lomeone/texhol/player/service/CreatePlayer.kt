package com.lomeone.texhol.player.service

import com.lomeone.texhol.player.entity.Player
import com.lomeone.texhol.player.exception.PlayerNicknameAlreadyExistException
import com.lomeone.texhol.player.repository.PlayerRepository
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service
import com.lomeone.texhol.common.TexholTransactional

@Service
class CreatePlayer(
    private val playerRepository: PlayerRepository
) {
    private val logger = KotlinLogging.logger {}

    @TexholTransactional
    operator fun invoke(command: CreatePlayerCommand): CreatePlayerResult {
        logger.info { "Creating player: nickname=${command.nickname}" }
        verifyNicknameDuplicate(command.nickname)
        val player = Player(nickname = command.nickname)
        val savedPlayer = playerRepository.save(player)
        logger.info { "Player created: id=${savedPlayer.id}, nickname=${savedPlayer.nickname}" }
        return CreatePlayerResult(id = savedPlayer.id)
    }

    private fun verifyNicknameDuplicate(nickname: String) {
        if (playerRepository.findByNickname(nickname) != null) {
            throw PlayerNicknameAlreadyExistException(detail = mapOf("nickname" to nickname))
        }
    }
}

data class CreatePlayerCommand(
    val nickname: String
)

data class CreatePlayerResult(
    val id: Long
)

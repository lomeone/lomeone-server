package com.lomeone.texhol.player.service

import com.lomeone.texhol.player.entity.Player
import com.lomeone.texhol.player.exception.PlayerNicknameAlreadyExistException
import com.lomeone.texhol.player.repository.PlayerRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CreatePlayer(
    private val playerRepository: PlayerRepository
) {
    @Transactional
    operator fun invoke(command: CreatePlayerCommand): CreatePlayerResult {
        verifyNicknameDuplicate(command.nickname)
        val player = Player(nickname = command.nickname)
        val savedPlayer = playerRepository.save(player)
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

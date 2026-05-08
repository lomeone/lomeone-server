package com.lomeone.texhol.player.service

import com.lomeone.texhol.player.entity.Player
import com.lomeone.texhol.player.exception.PlayerNotFoundException
import com.lomeone.texhol.player.repository.PlayerRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GetPlayerByNickname(
    private val playerRepository: PlayerRepository
) {
    @Transactional(readOnly = true)
    operator fun invoke(command: GetPlayerByNicknameCommand): Player {
        return playerRepository.findByNickname(command.nickname)
            ?: throw PlayerNotFoundException(detail = mapOf("nickname" to command.nickname))
    }
}

data class GetPlayerByNicknameCommand(
    val nickname: String
)

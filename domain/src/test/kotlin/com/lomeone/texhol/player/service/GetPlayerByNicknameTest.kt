package com.lomeone.texhol.player.service

import com.lomeone.texhol.player.entity.Player
import com.lomeone.texhol.player.exception.PlayerNotFoundException
import com.lomeone.texhol.player.repository.PlayerRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk

class GetPlayerByNicknameTest : BehaviorSpec({
    val playerRepository = mockk<PlayerRepository>()
    val getPlayerByNickname = GetPlayerByNickname(playerRepository)

    Given("특정 닉네임의 플레이어가 존재할 때") {
        val nickname = "홍길동"
        val player = Player(nickname = nickname)

        every { playerRepository.findByNickname(nickname) } returns player

        When("해당 닉네임으로 플레이어를 조회하면") {
            val command = GetPlayerByNicknameCommand(nickname = nickname)
            val result = getPlayerByNickname(command)

            Then("플레이어가 반환된다") {
                result.nickname shouldBe nickname
            }
        }
    }

    Given("특정 닉네임의 플레이어가 존재하지 않을 때") {
        val nickname = "존재하지않는닉네임"
        every { playerRepository.findByNickname(nickname) } returns null

        When("해당 닉네임으로 플레이어를 조회하면") {
            val command = GetPlayerByNicknameCommand(nickname = nickname)

            Then("PlayerNotFoundException이 발생한다") {
                shouldThrow<PlayerNotFoundException> {
                    getPlayerByNickname(command)
                }
            }
        }
    }
})

package com.lomeone.texhol.player.service

import com.lomeone.texhol.player.entity.Player
import com.lomeone.texhol.player.repository.PlayerRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class FindOrCreatePlayerTest : BehaviorSpec({
    val playerRepository = mockk<PlayerRepository>()
    val findOrCreatePlayer = FindOrCreatePlayer(playerRepository)

    Given("이미 존재하는 닉네임이 주어졌을 때") {
        val nickname = "홍길동"
        val existingPlayer = Player(nickname = nickname)

        every { playerRepository.findByNickname(nickname) } returns existingPlayer

        When("플레이어를 찾거나 생성하면") {
            val command = FindOrCreatePlayerCommand(nickname = nickname)
            val result = findOrCreatePlayer(command)

            Then("기존 플레이어가 반환되고 새로 생성하지 않는다") {
                result shouldBe existingPlayer
                verify(exactly = 0) {
                    playerRepository.save(any())
                }
            }
        }
    }

    Given("존재하지 않는 닉네임이 주어졌을 때") {
        val nickname = "홍길동"

        every { playerRepository.findByNickname(nickname) } returns null
        every { playerRepository.save(any()) } answers { firstArg() }

        When("플레이어를 찾거나 생성하면") {
            val command = FindOrCreatePlayerCommand(nickname = nickname)
            val result = findOrCreatePlayer(command)

            Then("새로운 플레이어가 생성된다") {
                result.nickname shouldBe nickname
                verify {
                    playerRepository.save(match { it.nickname == nickname })
                }
            }
        }
    }
})

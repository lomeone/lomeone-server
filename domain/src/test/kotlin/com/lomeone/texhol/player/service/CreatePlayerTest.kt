package com.lomeone.texhol.player.service

import com.lomeone.texhol.player.entity.Player
import com.lomeone.texhol.player.exception.PlayerNicknameAlreadyExistException
import com.lomeone.texhol.player.repository.PlayerRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class CreatePlayerTest : BehaviorSpec({
    val playerRepository = mockk<PlayerRepository>()
    val createPlayer = CreatePlayer(playerRepository)

    Given("중복되지 않은 닉네임이 주어졌을 때") {
        val nickname = "홍길동"

        every { playerRepository.findByNickname(nickname) } returns null
        every { playerRepository.save(any()) } answers { firstArg() }

        When("새로운 플레이어를 생성하면") {
            val command = CreatePlayerCommand(nickname = nickname)
            createPlayer(command)

            Then("올바른 플레이어 정보로 저장이 호출된다") {
                verify {
                    playerRepository.save(match {
                        it.nickname == nickname
                    })
                }
            }
        }
    }

    Given("이미 존재하는 닉네임이 주어졌을 때") {
        val nickname = "홍길동"
        val existingPlayer = Player(nickname = nickname)

        every { playerRepository.findByNickname(nickname) } returns existingPlayer

        When("동일한 닉네임으로 플레이어를 생성하려고 하면") {
            val command = CreatePlayerCommand(nickname = nickname)

            Then("PlayerNicknameAlreadyExistException이 발생한다") {
                shouldThrow<PlayerNicknameAlreadyExistException> {
                    createPlayer(command)
                }
            }
        }
    }

    Given("다양한 중복되지 않은 닉네임이 주어졌을 때") {
        every { playerRepository.findByNickname(any()) } returns null
        every { playerRepository.save(any()) } answers { firstArg() }

        When("여러 플레이어를 생성하면") {
            val command1 = CreatePlayerCommand(nickname = "홍길동")
            val command2 = CreatePlayerCommand(nickname = "김철수")
            val command3 = CreatePlayerCommand(nickname = "이영희")

            createPlayer(command1)
            createPlayer(command2)
            createPlayer(command3)

            Then("각각의 플레이어가 생성된다") {
                verify(atLeast = 3) {
                    playerRepository.save(any())
                }
            }
        }
    }
})

package com.lomeone.domain.authentication.service

import com.lomeone.domain.authentication.entity.Realm
import com.lomeone.domain.authentication.exception.RealmAlreadyExistException
import com.lomeone.domain.authentication.repository.RealmRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk

class CreateRealmTest : BehaviorSpec({
    val realmRepository: RealmRepository = mockk()
    val createRealm = CreateRealm(realmRepository)

    beforeTest {
        val mockRealm: Realm = mockk()
        every { mockRealm.code } returns "realm-code"
        every { realmRepository.save(any()) } returns mockRealm
    }

    Given("동일한 코드의 Realm이 없으면") {
        every { realmRepository.findByCode(any()) } returns null

        When("Realm을 생성할 때") {
            val command = CreateRealmCommand(
                name = "name",
                code = "realm-code"
            )

            val result = createRealm.execute(command)

            Then("Realm이 생성된다") {

                result.realmCode shouldBe "realm-code"
            }
        }
    }

    Given("코드를 입력하지 않으면") {
        When("Realm을 생성할 때") {
            val command = CreateRealmCommand(
                name = "name"
            )

            val result = createRealm.execute(command)

            Then("Realm이 생성된다") {
                result.realmCode shouldBe "realm-code"
            }
        }
    }

    Given("동일한 코드의 Realm이 있으면") {
        every { realmRepository.findByCode(any()) } returns mockk()

        When("Realm을 생성할 때") {
            val command = CreateRealmCommand(
                name = "name",
                code = "code"
            )

            Then("Realm이 이미 존재한다는 예외가 발생해서 Realm을 생성할 수 없다") {
                shouldThrow<RealmAlreadyExistException> {
                    createRealm.execute(command)
                }
            }
        }
    }
})

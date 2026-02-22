package com.lomeone.domain.authentication.service

import com.lomeone.authentication.entity.Realm
import com.lomeone.authentication.exception.RealmNotFoundException
import com.lomeone.authentication.repository.RealmRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk

class GetRealmByCodeTest : BehaviorSpec({
    val realmRepository: com.lomeone.authentication.repository.RealmRepository = mockk()
    val getRealmByCode = _root_ide_package_.com.lomeone.authentication.service.GetRealmByCode(realmRepository)

    Given("A realm with code 'test-realm' exists") {
        val codeInput = "test-realm"

        every { realmRepository.findByCode(codeInput) } returns _root_ide_package_.com.lomeone.authentication.entity.Realm(
            name = "Test Realm",
            code = codeInput,
        )

        When("GetRealmByCode is executed") {
            val query = _root_ide_package_.com.lomeone.authentication.service.GetRealmByCodeQuery(codeInput)
            val result = getRealmByCode.execute(query)

            Then("It should return the realm with the correct code") {
                result.code shouldBe codeInput
            }
        }
    }

    Given("A realm with code 'test-user-realm' doesn't exist") {
        val codeInput = "test-user-realm"

        every { realmRepository.findByCode(codeInput) } returns null

        When("GetRealmByCode is executed") {
            val query = _root_ide_package_.com.lomeone.authentication.service.GetRealmByCodeQuery(codeInput)

            Then("It should throw RealmNotFoundException") {
                shouldThrow<com.lomeone.authentication.exception.RealmNotFoundException> {
                    getRealmByCode.execute(query)
                }
            }
        }
    }
})

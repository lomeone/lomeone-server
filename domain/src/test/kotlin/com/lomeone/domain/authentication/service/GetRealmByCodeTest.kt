package com.lomeone.domain.authentication.service

import com.lomeone.domain.authentication.entity.Realm
import com.lomeone.domain.authentication.exception.RealmNotFoundException
import com.lomeone.domain.authentication.repository.RealmRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk

class GetRealmByCodeTest : BehaviorSpec({
    val realmRepository: RealmRepository = mockk()
    val getRealmByCode = GetRealmByCode(realmRepository)

    Given("A realm with code 'test-realm' exists") {
        val codeInput = "test-realm"

        every { realmRepository.findByCode(codeInput) } returns Realm(
            name = "Test Realm",
            code = codeInput,
        )

        When("GetRealmByCode is executed") {
            val query = GetRealmByCodeQuery(codeInput)
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
            val query = GetRealmByCodeQuery(codeInput)

            Then("It should throw RealmNotFoundException") {
                shouldThrow<RealmNotFoundException> {
                    getRealmByCode.execute(query)
                }
            }
        }
    }
})

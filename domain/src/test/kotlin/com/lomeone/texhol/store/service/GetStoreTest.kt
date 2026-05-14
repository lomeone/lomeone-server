package com.lomeone.texhol.store.service

import com.lomeone.texhol.store.entity.Store
import org.springframework.data.repository.findByIdOrNull
import com.lomeone.texhol.store.exception.StoreNotFoundException
import com.lomeone.texhol.store.repository.StoreRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk

class GetStoreTest : BehaviorSpec({
    val storeRepository = mockk<StoreRepository>()
    val getStore = GetStore(storeRepository)

    Given("특정 ID의 매장이 존재할 때") {
        val storeId = 1L
        val store = Store(
            name = "강남점",
            location = "서울 강남구",
            address = null,
            imageUrl = "https://example.com/store.jpg"
        )
        every { storeRepository.findByIdOrNull(storeId) } returns store

        When("해당 매장을 조회하면") {
            val command = GetStoreCommand(storeId = storeId)
            val result = getStore(command)

            Then("매장이 반환된다") {
                result.name shouldBe "강남점"
                result.location shouldBe "서울 강남구"
            }
        }
    }

    Given("특정 ID의 매장이 존재하지 않을 때") {
        val storeId = 999L
        every { storeRepository.findByIdOrNull(storeId) } returns null

        When("해당 매장을 조회하면") {
            val command = GetStoreCommand(storeId = storeId)

            Then("StoreNotFoundException이 발생한다") {
                shouldThrow<StoreNotFoundException> {
                    getStore(command)
                }
            }
        }
    }
})

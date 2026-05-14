package com.lomeone.texhol.store.service

import com.lomeone.texhol.store.entity.Store
import com.lomeone.texhol.store.repository.StoreRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk

class GetStoresTest : BehaviorSpec({
    val storeRepository = mockk<StoreRepository>()
    val getStores = GetStores(storeRepository)

    Given("여러 매장이 존재할 때") {
        val stores = listOf(
            Store("강남점", "서울 강남구", null, "https://example.com/store.jpg"),
            Store("홍대점", "서울 마포구", null, "https://example.com/store.jpg"),
            Store("신촌점", "서울 서대문구", null, "https://example.com/store.jpg")
        )
        every { storeRepository.findAll() } returns stores

        When("모든 매장 목록을 조회하면") {
            val result = getStores()

            Then("모든 매장이 반환된다") {
                result.size shouldBe 3
                result[0].name shouldBe "강남점"
                result[1].name shouldBe "홍대점"
                result[2].name shouldBe "신촌점"
            }
        }
    }

    Given("매장이 없을 때") {
        every { storeRepository.findAll() } returns emptyList()

        When("매장 목록을 조회하면") {
            val result = getStores()

            Then("빈 목록이 반환된다") {
                result.size shouldBe 0
            }
        }
    }
})

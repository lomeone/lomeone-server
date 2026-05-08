package com.lomeone.texhol.store.service

import com.lomeone.texhol.store.entity.Store
import com.lomeone.texhol.store.exception.StoreNameAlreadyExistException
import com.lomeone.texhol.store.repository.StoreRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class CreateStoreTest : BehaviorSpec({
    val storeRepository = mockk<StoreRepository>()
    val createStore = CreateStore(storeRepository)

    Given("중복되지 않는 매장 이름이 주어졌을 때") {
        val storeName = "강남점"
        every { storeRepository.findByName(storeName) } returns null
        every { storeRepository.save(any()) } answers { firstArg() }

        When("새로운 매장을 생성하면") {
            val command = CreateStoreCommand(
                name = storeName,
                location = "서울 강남구",
                address = null,
                imageUrl = "https://example.com/store.jpg"
            )
            val result = createStore(command)

            Then("매장이 생성되고 결과가 반환된다") {
                result.name shouldBe storeName
                result.location shouldBe "서울 강남구"
                verify {
                    storeRepository.save(match { it.name == storeName })
                }
            }
        }
    }

    Given("이미 존재하는 매장 이름이 주어졌을 때") {
        val storeName = "강남점"
        val existingStore = Store(
            name = storeName,
            location = "서울 강남구",
            address = null,
            imageUrl = "https://example.com/store.jpg"
        )
        every { storeRepository.findByName(storeName) } returns existingStore

        When("동일한 이름으로 매장을 생성하려고 하면") {
            val command = CreateStoreCommand(
                name = storeName,
                location = "서울 강남구",
                address = null,
                imageUrl = "https://example.com/store.jpg"
            )

            Then("StoreNameAlreadyExistException이 발생한다") {
                shouldThrow<StoreNameAlreadyExistException> {
                    createStore(command)
                }
            }
        }
    }
})

package com.lomeone.moyemap.service

import com.lomeone.moyemap.entity.Location
import com.lomeone.moyemap.entity.Venue
import com.lomeone.moyemap.entity.VenueCategory
import com.lomeone.moyemap.exception.VenueNotFoundException
import com.lomeone.moyemap.repository.VenueRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.util.Optional

class UpdateVenueTest : BehaviorSpec({
    val venueRepository = mockk<VenueRepository>()
    val updateVenue = UpdateVenue(venueRepository)

    Given("존재하는 Venue가 주어졌을 때") {
        val location = Location("홍대", "서울 마포구", 37.5571, 126.9243, "홍대/연남")
        val venue = Venue("원래 제목", VenueCategory.SOCIAL_PARTY, location, minPrice = 25000, imageUrl = "old.jpg", description = "원래 설명", sourceUrl = "old-url")

        every { venueRepository.findById(1L) } returns Optional.of(venue)
        every { venueRepository.save(any()) } answers { firstArg() }

        When("제목을 수정하면") {
            val command = UpdateVenueCommand(venueId = 1L, title = "새로운 제목")
            val result = updateVenue(command)

            Then("제목이 변경된다") {
                result.title shouldBe "새로운 제목"
                verify { venueRepository.save(venue) }
            }
        }

        When("카테고리를 수정하면") {
            val command = UpdateVenueCommand(venueId = 1L, category = VenueCategory.BAR)
            val result = updateVenue(command)

            Then("카테고리가 변경된다") {
                result.category shouldBe VenueCategory.BAR
            }
        }

        When("가격을 수정하면") {
            val command = UpdateVenueCommand(venueId = 1L, minPrice = 30000)
            val result = updateVenue(command)

            Then("가격이 변경된다") {
                result.minPrice shouldBe 30000
            }
        }

        When("여러 필드를 동시에 수정하면") {
            val command = UpdateVenueCommand(
                venueId = 1L,
                title = "업데이트된 제목",
                minPrice = 40000,
                description = "업데이트된 설명"
            )
            val result = updateVenue(command)

            Then("모든 필드가 변경된다") {
                result.title shouldBe "업데이트된 제목"
                result.minPrice shouldBe 40000
                result.description shouldBe "업데이트된 설명"
            }
        }

        When("위치 정보를 수정하면") {
            val command = UpdateVenueCommand(
                venueId = 1L,
                locationName = "강남역 11번 출구",
                address = "서울특별시 강남구 테헤란로 123",
                latitude = 37.4979,
                longitude = 127.0276,
                region = "강남"
            )
            val result = updateVenue(command)

            Then("위치 정보가 변경된다") {
                result.location.name shouldBe "강남역 11번 출구"
                result.location.address shouldBe "서울특별시 강남구 테헤란로 123"
                result.location.region shouldBe "강남"
            }
        }

        When("태그를 수정하면") {
            val command = UpdateVenueCommand(
                venueId = 1L,
                tags = listOf("루프탑", "와인")
            )
            val result = updateVenue(command)

            Then("태그가 변경된다") {
                result.tags shouldBe listOf("루프탑", "와인")
            }
        }
    }

    Given("존재하지 않는 Venue ID가 주어졌을 때") {
        every { venueRepository.findById(999L) } returns Optional.empty()

        When("해당 Venue를 수정하려고 하면") {
            val command = UpdateVenueCommand(venueId = 999L, title = "새 제목")

            Then("VenueNotFoundException이 발생한다") {
                shouldThrow<VenueNotFoundException> {
                    updateVenue(command)
                }
            }
        }
    }
})

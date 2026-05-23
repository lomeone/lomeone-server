package com.lomeone.moyemap.entity

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

class ReviewTest : FreeSpec({
    "Review 생성" - {
        val location = Location("test", "address", 0.0, 0.0, "region")
        val venue = Venue(
            title = "venue",
            category = VenueCategory.SOCIAL_PARTY,
            location = location,
            price = 10000,
            imageUrl = "url",
            description = "desc",
            sourceUrl = "source"
        )

        "필수 정보로 리뷰를 생성할 수 있다" {
            val review = Review(
                venue = venue,
                userName = "김민수",
                rating = 5,
                comment = "분위기 너무 좋아요!"
            )

            review.venue shouldBe venue
            review.userName shouldBe "김민수"
            review.rating shouldBe 5
            review.comment shouldBe "분위기 너무 좋아요!"
        }

        "평점은 1-5 사이여야 한다" {
            Review(venue, "user", 1, "comment").rating shouldBe 1
            Review(venue, "user", 3, "comment").rating shouldBe 3
            Review(venue, "user", 5, "comment").rating shouldBe 5
        }

        "평점이 1보다 작으면 예외가 발생한다" {
            shouldThrow<IllegalArgumentException> {
                Review(venue, "user", 0, "comment")
            }
        }

        "평점이 5보다 크면 예외가 발생한다" {
            shouldThrow<IllegalArgumentException> {
                Review(venue, "user", 6, "comment")
            }
        }
    }

    "Review 수정" - {
        val location = Location("test", "address", 0.0, 0.0, "region")
        val venue = Venue(
            title = "venue",
            category = VenueCategory.SOCIAL_PARTY,
            location = location,
            price = 10000,
            imageUrl = "url",
            description = "desc",
            sourceUrl = "source"
        )
        val review = Review(venue, "김민수", 4, "좋아요")

        "사용자 이름을 수정할 수 있다" {
            review.updateReview(userName = "이영희")
            review.userName shouldBe "이영희"
        }

        "평점을 수정할 수 있다" {
            review.updateReview(rating = 5)
            review.rating shouldBe 5
        }

        "코멘트를 수정할 수 있다" {
            review.updateReview(comment = "정말 좋아요!")
            review.comment shouldBe "정말 좋아요!"
        }

        "여러 필드를 동시에 수정할 수 있다" {
            review.updateReview(
                userName = "박철수",
                rating = 3,
                comment = "괜찮아요"
            )

            review.userName shouldBe "박철수"
            review.rating shouldBe 3
            review.comment shouldBe "괜찮아요"
        }

        "평점 수정 시에도 1-5 범위를 벗어나면 예외가 발생한다" {
            shouldThrow<IllegalArgumentException> {
                review.updateReview(rating = 0)
            }

            shouldThrow<IllegalArgumentException> {
                review.updateReview(rating = 6)
            }
        }
    }
})

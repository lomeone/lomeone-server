package com.lomeone.moyemap.entity

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

class VenueCategoryTest : FreeSpec({
    "VenueCategory Enum" - {
        "9개의 카테고리가 정의되어 있다" {
            VenueCategory.entries.size shouldBe 9
        }

        "모든 카테고리가 정의되어 있다" {
            VenueCategory.entries.map { it.name } shouldBe listOf(
                "SOCIAL_PARTY",
                "SOLO_PARTY",
                "GUESTHOUSE_PARTY",
                "ROTATION_DATING",
                "NETWORKING",
                "HONSOOL_BAR",
                "BAR",
                "WORKSHOP",
                "ETC"
            )
        }
    }
})

package com.lomeone.domain.user.entity

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

class DeletionRequestTest : FreeSpec({
    "삭제 요청을 할 수 있다" - {
        val deletionRequest = DeletionRequest(
            userToken = "user1234",
            reason = "delete user"
        )

        deletionRequest.status shouldBe DeletionStatus.REQUEST

        "삭제 요청을 취소하고 복구할 수 있다" - {
            deletionRequest.restore()
            deletionRequest.status shouldBe DeletionStatus.RESTORE
        }
    }
})

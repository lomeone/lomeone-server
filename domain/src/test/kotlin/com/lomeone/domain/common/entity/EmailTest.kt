package com.lomeone.domain.common.entity

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

class EmailTest : FreeSpec({
    "이메일 생성할 때" {
        "이메일이 공백이 아니고 형식에 맞으면 생성된다" - {
            val inputValue = "email@gmail.com"
            val email = Email(inputValue)

            email.value shouldBe  inputValue
        }
    }
})

package com.lomeone.domain.common.entity

import com.lomeone.domain.common.exception.EmailAddressInvalidException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

class EmailTest : FreeSpec({
    "이메일 생성할 때" - {
        "이메일이 공백이 아니고 형식에 맞으면 생성된다" - {
            val inputValue = "email@gmail.com"
            val email = Email(inputValue)

            email.value shouldBe inputValue
        }

        "이메일이 공백이면 이메일이 공백이라는 예외가 발생한다" - {
            val inputValue = ""

            shouldThrow<EmailAddressInvalidException> {
                Email(inputValue)
            }
        }

        "이메일이 형식에 맞지 않으면 이메일이 형식에 맞지 않는다는 예외가 발생한다" - {
            val inputValue = "email"

            shouldThrow<EmailAddressInvalidException> {
                Email(inputValue)
            }
        }
    }
})

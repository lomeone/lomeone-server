package com.lomeone.util.security.authentication

import com.lomeone.util.security.authentication.PasswordUtils.checkPasswordValidity
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec

class PasswordUtilsTest : FreeSpec({
    "비밀번호의 형식이 맞는지 검증할 수 있다" - {
        shouldThrow<IllegalArgumentException> {
            checkPasswordValidity("")
        }

        shouldThrow<IllegalArgumentException> {
            checkPasswordValidity("abcd1234")
        }

        checkPasswordValidity("testPassword1324@")
    }
})

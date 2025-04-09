package com.lomeone.util.security.crypto

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class AESCryptoTest : FreeSpec({
    "문자열을 암호화하고 복호화할 수 있다" - {
        val encrypted = AESCrypto.encrypt("test", "1234567890abcdef")
        val decrypted = AESCrypto.decrypt(encrypted, "1234567890abcdef")
        encrypted shouldNotBe "test"
        decrypted shouldBe "test"
    }
})

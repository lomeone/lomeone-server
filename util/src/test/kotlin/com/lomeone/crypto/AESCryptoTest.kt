package com.lomeone.crypto

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class AESCryptoTest : FreeSpec({
    "암호화된 문자열을 복호화할 수 있다." - {
        val crypto = AESCrypto("1234567890abcdef")
        val encrypted = crypto.encrypt("test")
        val decrypted = crypto.decrypt(encrypted)
        encrypted shouldNotBe "test"
        decrypted shouldBe "test"
    }
})

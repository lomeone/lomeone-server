package io.github.comstering.converter

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class CryptoConverterTest : FreeSpec({
    val cryptoConverter = CryptoConverter()

    "convertToDatabaseColumn" - {
        "암호화된 문자열을 반환한다" {
            val attribute = "test"
            val dbData = cryptoConverter.convertToDatabaseColumn(attribute)
            dbData shouldNotBe "test"
        }
    }
    "convertToEntityAttribute" - {
        "복호화된 문자열을 반환한다" {
            val attribute = "test"
            val dbData = cryptoConverter.convertToDatabaseColumn(attribute)
            val decryptedAttribute = cryptoConverter.convertToEntityAttribute(dbData)
            decryptedAttribute shouldBe "test"
        }
    }
})

package com.lomeone.util.converter

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class EmailCryptoConverterTest : FreeSpec({
    val converter = EmailCryptoConverter()

    "convertToDatabaseColumn" - {
        "암호화하여 저장한다" {
            val attribute = "test"
            val dbData = converter.convertToDatabaseColumn(attribute)
            dbData shouldNotBe "test"
        }
    }
    "convertToEntityAttribute" - {
        "복호화하여 가져온다" {
            val attribute = "test"
            val dbData = converter.convertToDatabaseColumn(attribute)
            val decryptedAttribute = converter.convertToEntityAttribute(dbData)
            decryptedAttribute shouldBe "test"
        }
    }
})

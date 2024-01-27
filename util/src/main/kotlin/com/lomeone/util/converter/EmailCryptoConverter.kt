package com.lomeone.util.converter

import com.lomeone.util.security.crypto.AESCrypto
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

@Converter
class EmailCryptoConverter : AttributeConverter<String,String> {

    private val crypto = AESCrypto("1234567890abcdef")

    override fun convertToDatabaseColumn(attribute: String) = this.crypto.encrypt(attribute)

    override fun convertToEntityAttribute(dbData: String) = this.crypto.decrypt(dbData)
}

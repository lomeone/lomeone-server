package com.lomeone.util.converter

import com.lomeone.util.security.crypto.AESCrypto
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

@Converter
class EmailCryptoConverter : AttributeConverter<String,String> {

//    @Value("\${email.cryptoKey}")
    private val cryptoKey: String = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890"

    override fun convertToDatabaseColumn(attribute: String) = AESCrypto.encrypt(attribute, cryptoKey)

    override fun convertToEntityAttribute(dbData: String) = AESCrypto.decrypt(dbData, cryptoKey)
}

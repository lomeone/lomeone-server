package com.lomeone.util.converter

import com.lomeone.eunoia.kotlin.util.security.crypto.symmetric.AESGCMCrypto
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

@Converter
class EmailCryptoConverter : AttributeConverter<String,String> {

//    @Value("\${email.cryptoKey}")
    private val cryptoKey: String = "ABCDEFGHIJKLMNOPQRSTUVWXYZ123456"

    override fun convertToDatabaseColumn(attribute: String) = AESGCMCrypto.encrypt(attribute, cryptoKey)

    override fun convertToEntityAttribute(dbData: String) = AESGCMCrypto.decrypt(dbData, cryptoKey)
}

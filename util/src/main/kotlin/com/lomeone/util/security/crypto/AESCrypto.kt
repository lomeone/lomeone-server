package com.lomeone.util.security.crypto

import java.nio.charset.StandardCharsets
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec

private const val ALGORITHM: String = "AES/CBC/PKCS5Padding"
private val IV: IvParameterSpec = IvParameterSpec(byteArrayOf(0x30, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x61, 0x62, 0x63, 0x64, 0x65, 0x66))

object AESCrypto : Crypto {
    override fun encrypt(plainText: String, key: String): String {
        val cipher = Cipher.getInstance(ALGORITHM)
        cipher.init(Cipher.ENCRYPT_MODE, getKeySpec(key, "AES"), IV)
        return String(Base64.getEncoder().encode(cipher.doFinal(plainText.toByteArray())), StandardCharsets.UTF_8)
    }

    override fun decrypt(cipherText: String, key: String): String {
        val cipher = Cipher.getInstance(ALGORITHM)
        cipher.init(Cipher.DECRYPT_MODE, getKeySpec(key, "AES"), IV)
        return String(cipher.doFinal(Base64.getDecoder().decode(cipherText)), StandardCharsets.UTF_8)
    }
}

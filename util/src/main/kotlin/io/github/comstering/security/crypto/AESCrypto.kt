package io.github.comstering.security.crypto

import java.nio.charset.StandardCharsets
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class AESCrypto(
    keyString: String
) {
    private val key: SecretKeySpec = SecretKeySpec(keyString.toByteArray(), "AES")

    companion object {
        private const val ALGORITHM: String = "AES/CBC/PKCS5Padding"
        private val IV: IvParameterSpec = IvParameterSpec(byteArrayOf(0x30, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x61, 0x62, 0x63, 0x64, 0x65, 0x66))
    }

    fun encrypt(plainText: String): String {
        val cipher = Cipher.getInstance(ALGORITHM)
        cipher.init(Cipher.ENCRYPT_MODE, this.key, IV)
        return String(Base64.getEncoder().encode(cipher.doFinal(plainText.toByteArray())), StandardCharsets.UTF_8)
    }

    fun decrypt(cipherText: String): String {
        val cipher = Cipher.getInstance(ALGORITHM)
        cipher.init(Cipher.DECRYPT_MODE, this.key, IV)
        return String(cipher.doFinal(Base64.getDecoder().decode(cipherText)), StandardCharsets.UTF_8)
    }
}

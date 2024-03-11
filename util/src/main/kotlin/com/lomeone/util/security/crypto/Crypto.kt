package com.lomeone.util.security.crypto

import javax.crypto.spec.SecretKeySpec

fun getKeySpec(key: String, algorithm: String): SecretKeySpec = SecretKeySpec(key.toByteArray(), algorithm)

interface Crypto {
    fun encrypt(plainText: String, key: String): String
    fun decrypt(cipherText: String, key: String): String
}

package com.lomeone.util.security.crypto

import javax.crypto.spec.SecretKeySpec

interface Crypto {
    fun encrypt(plainText: String, key: String): String
    fun decrypt(cipherText: String, key: String): String
}

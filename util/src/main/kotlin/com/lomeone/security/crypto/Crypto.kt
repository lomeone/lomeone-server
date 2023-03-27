package com.lomeone.security.crypto

import javax.crypto.spec.SecretKeySpec

interface Crypto {
    fun encrypt(plainText: String): String
    fun decrypt(cipherText: String): String
}

package com.lomeone.util.string

object RandomStringUtil {
    fun generateRandomString(charSet: Set<Char>, length: Int): String =
        (1..length).map { charSet.random() }
            .joinToString("")
}

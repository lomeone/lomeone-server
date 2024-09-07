package com.lomeone.util.string

object RandomStringUtil {
    fun createRandomString(charSet: Set<Char>, length: Int): String =
        (1..length).map { charSet.random() }
            .joinToString("")
}

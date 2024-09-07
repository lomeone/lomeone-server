package com.lomeone.util.string

object RandomString {
    fun makeRandomString(charSet: Set<Char>, length: Int): String =
        (1..length).map { charSet.random() }
            .joinToString("")
}

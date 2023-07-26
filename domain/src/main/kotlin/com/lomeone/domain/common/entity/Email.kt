package com.lomeone.domain.common.entity

@JvmInline
value class Email(val value: String) {
    init {
        checkValidity(value)
    }

    private fun checkValidity(email: String) {
        email.isBlank() && throw Exception("Invalid email address")
        checkFormatValid(email)
    }

    private fun checkFormatValid(email: String) {
        val regex = Regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")
        !regex.matches(email) && throw Exception("Invalid email address")
    }
}

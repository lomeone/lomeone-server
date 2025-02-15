package com.lomeone.domain.common.entity

import com.lomeone.domain.common.exception.EmailAddressInvalidException

@JvmInline
value class Email(val value: String) {
    init {
        checkValidity(value)
    }

    private fun checkValidity(email: String) {
        email.isBlank() && throw EmailAddressInvalidException(mapOf("email" to email))
        checkFormatValid(email)
    }

    private fun checkFormatValid(email: String) {
        val regex = Regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")
        !regex.matches(email) && throw EmailAddressInvalidException(mapOf("email" to email))
    }
}

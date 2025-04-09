package com.lomeone.util.security.authentication

object PasswordUtils {
    fun checkPasswordValidity(password: String) {
        checkPasswordIsNotBlank(password)
        checkPasswordFormatValid(password)
    }

    private fun checkPasswordIsNotBlank(password: String) {
        password.isBlank() && throw IllegalArgumentException("password must not be blank")
    }

    private fun checkPasswordFormatValid(password: String) {
        val regex = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[\$@\$!%*?&])[A-Za-z\\d\$@!%*?&]{10,}")
        !regex.matches(password) && throw IllegalArgumentException("password must be at least 10 characters, including at least one uppercase letter, one lowercase letter, one number and one special character")
    }
}

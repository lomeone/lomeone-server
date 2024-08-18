package com.lomeone.domain.authentication.exception

import com.lomeone.util.exception.CustomException
import com.lomeone.util.exception.ExceptionCategory

class AuthenticationPasswordInvalidException(
    message: String = MESSAGE,
    detail: Map<String, Any>
) : CustomException(
    errorCode = ERROR_CODE,
    message = message,
    exceptionCategory = ExceptionCategory.BAD_REQUEST,
    detail = detail
) {
    companion object {
        const val ERROR_CODE = "authentication/invalid-password"
        const val MESSAGE = "Authentication invalid password"
    }
}
package com.lomeone.domain.authentication.exception

import com.lomeone.util.exception.CustomException
import com.lomeone.util.exception.ExceptionCategory

class AuthenticationNotFoundException(
    detail: Map<String, Any>
) : CustomException(
    errorCode = ERROR_CODE,
    message = MESSAGE,
    exceptionCategory = ExceptionCategory.NOT_FOUND,
    detail = detail
) {
    companion object {
        const val ERROR_CODE = "authentication/not-found"
        const val MESSAGE = "Authentication not found"
    }
}

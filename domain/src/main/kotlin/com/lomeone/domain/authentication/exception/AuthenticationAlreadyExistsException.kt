package com.lomeone.domain.authentication.exception

import com.lomeone.util.exception.CustomException
import com.lomeone.util.exception.ExceptionCategory

class AuthenticationAlreadyExistsException(
    detail: Map<String, Any>
) : CustomException(
    errorCode = ERROR_CODE,
    message = MESSAGE,
    exceptionCategory = ExceptionCategory.BAD_REQUEST
) {
    companion object {
        const val ERROR_CODE = "authentication/already-exists"
        const val MESSAGE = ""
    }
}

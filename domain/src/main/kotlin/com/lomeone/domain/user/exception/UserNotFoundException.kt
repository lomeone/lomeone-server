package com.lomeone.domain.user.exception

import com.lomeone.util.exception.CustomException
import com.lomeone.util.exception.ExceptionCategory

class UserNotFoundException(
    detail: Map<String, Any>
) : CustomException(
    errorCode = ERROR_CODE,
    message = MESSAGE,
    exceptionCategory = ExceptionCategory.NOT_FOUND,
    detail = detail
) {
    companion object {
        const val ERROR_CODE = "user/not-found"
        const val MESSAGE = "User not found"
    }
}

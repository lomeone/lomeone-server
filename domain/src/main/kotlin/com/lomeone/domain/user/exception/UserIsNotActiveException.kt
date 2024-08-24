package com.lomeone.domain.user.exception

import com.lomeone.util.exception.CustomException
import com.lomeone.util.exception.ExceptionCategory

class UserIsNotActiveException(
    detail: Map<String, Any>
) : CustomException(
    errorCode = ERROR_CODE,
    message = MESSAGE,
    exceptionCategory = ExceptionCategory.FORBIDDEN,
    detail = detail
) {
    companion object {
        const val ERROR_CODE = "user/is-not-active"
        const val MESSAGE = "User is not active"
    }
}
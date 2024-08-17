package com.lomeone.domain.user.exception

import com.lomeone.util.exception.CustomException
import com.lomeone.util.exception.ExceptionCategory


class UserEmailAlreadyExistsException(
    detail: Map<String, Any>
) : CustomException(
    errorCode = ERROR_CODE,
    message = MESSAGE,
    exceptionCategory = ExceptionCategory.BAD_REQUEST,
    detail = detail
) {
    companion object {
        const val ERROR_CODE = "user/email-already-exists"
        const val MESSAGE = "User with the same email already exists"
    }
}

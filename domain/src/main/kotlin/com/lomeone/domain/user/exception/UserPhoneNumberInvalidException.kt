package com.lomeone.domain.user.exception

import com.lomeone.util.exception.CustomException
import com.lomeone.util.exception.ExceptionCategory

class UserPhoneNumberInvalidException(
    message: String = MESSAGE,
    detail: Map<String, Any>
) : CustomException(
    errorCode = ERROR_CODE,
    message = message,
    exceptionCategory = ExceptionCategory.BAD_REQUEST,
    detail = detail
) {
    companion object {
        const val ERROR_CODE = "user/phone-number-invalid"
        const val MESSAGE = "Invalid phone number"
    }
}

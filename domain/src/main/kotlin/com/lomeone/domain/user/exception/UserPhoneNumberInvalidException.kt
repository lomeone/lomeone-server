package com.lomeone.domain.user.exception

import com.lomeone.eunoia.exception.ErrorCode
import com.lomeone.eunoia.exception.EunioaException
import com.lomeone.eunoia.exception.ExceptionCategory
import com.lomeone.eunoia.exception.ExceptionDetail

private const val MESSAGE = "Invalid phone number"
private val ERROR_CODE = ErrorCode(
    code = "user/phone-number-invalid",
    exceptionCategory = ExceptionCategory.BAD_REQUEST
)

class UserPhoneNumberInvalidException(
    message: String = MESSAGE,
    detail: Map<String, Any>
) : EunioaException(
    message = message,
    errorCode = ERROR_CODE,
    detail = ExceptionDetail(detail)
)

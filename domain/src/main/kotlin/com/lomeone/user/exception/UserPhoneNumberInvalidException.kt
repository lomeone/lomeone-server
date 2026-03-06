package com.lomeone.user.exception

import com.lomeone.eunoia.exception.EunioaException
import com.lomeone.eunoia.exception.ExceptionDetail

private const val MESSAGE = "User phone number is invalid"

class UserPhoneNumberInvalidException(
    message: String = MESSAGE,
    detail: Map<String, Any>
) : EunioaException(
    message = message,
    errorCode = UserErrorCode.PHONE_NUMBER_INVALID,
    detail = ExceptionDetail(detail)
)

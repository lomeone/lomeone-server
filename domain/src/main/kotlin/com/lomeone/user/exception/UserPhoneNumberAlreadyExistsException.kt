package com.lomeone.user.exception

import com.lomeone.eunoia.exception.EunioaException
import com.lomeone.eunoia.exception.ExceptionDetail

private const val MESSAGE = "User with the same phone number already exists"

class UserPhoneNumberAlreadyExistsException(
    message: String = MESSAGE,
    detail: Map<String, Any>
) : EunioaException(
    message = message,
    errorCode = UserErrorCode.PHONE_NUMBER_ALREADY_EXISTS,
    detail = ExceptionDetail(detail)
)

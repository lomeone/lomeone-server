package com.lomeone.user.exception

import com.lomeone.eunoia.exception.EunioaException
import com.lomeone.eunoia.exception.ExceptionDetail

private const val MESSAGE = "User with the same email already exists"

class UserEmailAlreadyExistsException(
    message: String = MESSAGE,
    detail: Map<String, Any>
) : EunioaException(
    message = message,
    errorCode = UserErrorCode.EMAIL_ALREADY_EXISTS,
    detail = ExceptionDetail(detail)
)

package com.lomeone.user.exception

import com.lomeone.eunoia.exception.EunioaException
import com.lomeone.eunoia.exception.ExceptionDetail

private const val MESSAGE = "User not found"

class UserNotFoundException(
    message: String = MESSAGE,
    detail: Map<String, Any>
) : EunioaException(
    message = message,
    errorCode = UserErrorCode.NOT_FOUND,
    detail = ExceptionDetail(detail)
)

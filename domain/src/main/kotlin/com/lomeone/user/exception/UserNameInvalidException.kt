package com.lomeone.user.exception

import com.lomeone.eunoia.exception.EunioaException
import com.lomeone.eunoia.exception.ExceptionDetail

private const val MESSAGE = "User name is invalid"

class UserNameInvalidException(
    message: String = MESSAGE,
    detail: Map<String, Any>
) : EunioaException(
    message = message,
    errorCode = UserErrorCode.NAME_INVALID,
    detail = ExceptionDetail(detail)
)

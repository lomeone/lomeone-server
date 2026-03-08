package com.lomeone.user.exception

import com.lomeone.eunoia.exception.EunioaException
import com.lomeone.eunoia.exception.ExceptionDetail

private const val MESSAGE = "User role must be not empty"

class UserRoleEmptyException(
    message: String = MESSAGE,
    detail: Map<String, Any>
) : EunioaException(
    message = message,
    errorCode = UserErrorCode.ROLE_EMPTY,
    detail = ExceptionDetail(detail)
)

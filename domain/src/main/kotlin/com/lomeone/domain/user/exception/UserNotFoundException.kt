package com.lomeone.domain.user.exception

import com.lomeone.eunoia.exception.ErrorCode
import com.lomeone.eunoia.exception.EunioaException
import com.lomeone.eunoia.exception.ExceptionCategory
import com.lomeone.eunoia.exception.ExceptionDetail

private const val MESSAGE = "User not found"
private val ERROR_CODE = ErrorCode(
    code = "user/not-found",
    exceptionCategory = ExceptionCategory.NOT_FOUND
)

class UserNotFoundException(
    detail: Map<String, Any>
) : EunioaException(
    message = MESSAGE,
    errorCode = ERROR_CODE,
    detail = ExceptionDetail(detail)
)

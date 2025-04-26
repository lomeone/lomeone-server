package com.lomeone.domain.authentication.exception

import com.lomeone.eunoia.exception.ErrorCode
import com.lomeone.eunoia.exception.EunioaException
import com.lomeone.eunoia.exception.ExceptionCategory
import com.lomeone.eunoia.exception.ExceptionDetail

private const val MESSAGE = "Authentication not found"
private val ERROR_CODE = ErrorCode(
    code = "authentication/not-found",
    exceptionCategory = ExceptionCategory.NOT_FOUND
)

class AuthenticationNotFoundException(
    detail: Map<String, Any>
) : EunioaException(
    message = MESSAGE,
    errorCode = ERROR_CODE,
    detail = ExceptionDetail(detail)
)

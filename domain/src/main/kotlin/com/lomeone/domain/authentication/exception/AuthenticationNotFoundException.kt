package com.lomeone.domain.authentication.exception

import com.lomeone.util.exception.CustomException
import com.lomeone.util.exception.ExceptionCategory

const val errorCode = "authentication/not-found"
const val exceptionMessage = "Authentication not found"

class AuthenticationNotFoundException(
    detail: Map<String, Any>
) : CustomException(
    errorCode = errorCode,
    message = exceptionMessage,
    exceptionCategory = ExceptionCategory.NOT_FOUND,
    detail = detail
)

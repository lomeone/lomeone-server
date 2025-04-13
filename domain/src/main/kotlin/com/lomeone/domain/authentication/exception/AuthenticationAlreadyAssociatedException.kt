package com.lomeone.domain.authentication.exception

import com.lomeone.util.exception.CustomException
import com.lomeone.util.exception.ExceptionCategory

private const val MESSAGE = "Authentication already associated"
private const val ERROR_CODE = "authentication/already-associated"

class AuthenticationAlreadyAssociatedException(
    message: String = MESSAGE,
    detail: Map<String, Any>,
) : CustomException(
    errorCode = ERROR_CODE,
    message = MESSAGE,
    exceptionCategory = ExceptionCategory.BAD_REQUEST,
    detail = detail,
)

package com.lomeone.domain.authentication.exception

import com.lomeone.eunoia.exception.ErrorCode
import com.lomeone.eunoia.exception.EunioaException
import com.lomeone.eunoia.exception.ExceptionCategory
import com.lomeone.eunoia.exception.ExceptionDetail

private const val MESSAGE = "Authentication provider must be EMAIL"
private val ERROR_CODE = ErrorCode(
    code = "authentication/is-not-email-provider",
    exceptionCategory = ExceptionCategory.BAD_REQUEST
)

class AuthenticationProviderIsNotEmailException(
    detail: Map<String, Any>
) : EunioaException(
    message = MESSAGE,
    errorCode = ERROR_CODE,
    detail = ExceptionDetail(detail)
)

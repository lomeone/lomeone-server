package com.lomeone.authentication.exception

import com.lomeone.eunoia.exception.ErrorCode
import com.lomeone.eunoia.exception.EunioaException
import com.lomeone.eunoia.exception.ExceptionCategory
import com.lomeone.eunoia.exception.ExceptionDetail

private const val MESSAGE = "Not yet supported oauth2 provider"
private val ERROR_CODE = ErrorCode(
    code = "authentication/oauth-provider-not-supported",
    exceptionCategory = ExceptionCategory.BAD_REQUEST
)

class OAuth2ProviderNotSupportedException(
    detail: Map<String, Any>
) : EunioaException(
    message = _root_ide_package_.com.lomeone.authentication.exception.MESSAGE,
    errorCode = _root_ide_package_.com.lomeone.authentication.exception.ERROR_CODE,
    detail = ExceptionDetail(detail)
)

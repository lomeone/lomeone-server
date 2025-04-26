package com.lomeone.domain.user.exception

import com.lomeone.eunoia.exception.ErrorCode
import com.lomeone.eunoia.exception.EunioaException
import com.lomeone.eunoia.exception.ExceptionCategory
import com.lomeone.eunoia.exception.ExceptionDetail

private const val MESSAGE = "Invalid name"
private val ERROR_CODE = ErrorCode(
    code = "user/name-invalid",
    exceptionCategory = ExceptionCategory.BAD_REQUEST
)

class UserNameInvalidException(
    message: String = MESSAGE,
    detail: Map<String, Any>
) : EunioaException(
    message = message,
    errorCode = ERROR_CODE,
    detail = ExceptionDetail(detail)
)

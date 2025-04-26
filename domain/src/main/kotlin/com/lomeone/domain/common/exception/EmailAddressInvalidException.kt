package com.lomeone.domain.common.exception

import com.lomeone.eunoia.exception.ErrorCode
import com.lomeone.eunoia.exception.EunioaException
import com.lomeone.eunoia.exception.ExceptionCategory
import com.lomeone.eunoia.exception.ExceptionDetail

private const val MESSAGE = "Invalid email address"
private val ERROR_CODE = ErrorCode(
    code = "common/email-invalid-exception",
    exceptionCategory = ExceptionCategory.BAD_REQUEST
)

class EmailAddressInvalidException(
    detail: Map<String, Any>
) : EunioaException(
    message = MESSAGE,
    errorCode = ERROR_CODE,
    detail = ExceptionDetail(detail)
)

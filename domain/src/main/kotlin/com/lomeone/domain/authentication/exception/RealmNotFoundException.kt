package com.lomeone.domain.authentication.exception

import com.lomeone.eunoia.exception.ErrorCode
import com.lomeone.eunoia.exception.EunioaException
import com.lomeone.eunoia.exception.ExceptionCategory
import com.lomeone.eunoia.exception.ExceptionDetail

private const val MESSAGE = "Realm not found"
private val ERROR_CODE = ErrorCode(
    code = "realm/not-found",
    exceptionCategory = ExceptionCategory.NOT_FOUND
)

class RealmNotFoundException(
    detail: Map<String, Any>
) : EunioaException(
    message = MESSAGE,
    errorCode = ERROR_CODE,
    detail = ExceptionDetail(detail)
)

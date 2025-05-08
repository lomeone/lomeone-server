package com.lomeone.domain.authentication.exception

import com.lomeone.eunoia.exception.ErrorCode
import com.lomeone.eunoia.exception.EunioaException
import com.lomeone.eunoia.exception.ExceptionCategory
import com.lomeone.eunoia.exception.ExceptionDetail

private const val MESSAGE = "Realm already exists."
private val ERROR_CODE = ErrorCode(
    code = "realm/already-exist",
    exceptionCategory = ExceptionCategory.BAD_REQUEST
)

class RealmAlreadyExistException(
    detail: Map<String, Any>
) : EunioaException(
    message = MESSAGE,
    errorCode = ERROR_CODE,
    detail = ExceptionDetail(detail)
)

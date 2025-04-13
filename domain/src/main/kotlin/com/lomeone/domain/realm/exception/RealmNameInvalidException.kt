package com.lomeone.domain.realm.exception

import com.lomeone.util.exception.CustomException
import com.lomeone.util.exception.ExceptionCategory

private const val ERROR_CODE = "realm/name-invalid"
private const val MESSAGE = "Invalid realm name"

class RealmNameInvalidException(
    message: String = MESSAGE,
    detail: Map<String, Any>
) : CustomException(
    errorCode = ERROR_CODE,
    message = message,
    exceptionCategory = ExceptionCategory.BAD_REQUEST,
    detail = detail
)

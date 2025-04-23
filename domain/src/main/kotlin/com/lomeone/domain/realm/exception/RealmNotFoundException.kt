package com.lomeone.domain.realm.exception

import com.lomeone.util.exception.CustomException
import com.lomeone.util.exception.ExceptionCategory

class RealmNotFoundException(
    detail: Map<String, Any>
) : CustomException(
    errorCode = ERROR_CODE,
    message = MESSAGE,
    exceptionCategory = ExceptionCategory.NOT_FOUND,
    detail = detail
) {
    companion object {
        const val ERROR_CODE = "realm/not-found"
        const val MESSAGE = "Realm not found"
    }
}

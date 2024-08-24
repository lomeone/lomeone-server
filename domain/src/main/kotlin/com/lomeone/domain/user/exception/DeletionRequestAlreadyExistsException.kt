package com.lomeone.domain.user.exception

import com.lomeone.util.exception.CustomException
import com.lomeone.util.exception.ExceptionCategory

class DeletionRequestAlreadyExistsException(
    detail: Map<String, Any>
) : CustomException(
    errorCode = ERROR_CODE,
    message = MESSAGE,
    exceptionCategory = ExceptionCategory.BAD_REQUEST,
    detail = detail
) {
    companion object {
        const val ERROR_CODE = "user/deletion-request-already-exists"
        const val MESSAGE = "Deletion request already exists"
    }
}
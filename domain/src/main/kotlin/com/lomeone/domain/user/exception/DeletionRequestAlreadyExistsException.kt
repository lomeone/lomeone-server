package com.lomeone.domain.user.exception

import com.lomeone.eunoia.exception.ErrorCode
import com.lomeone.eunoia.exception.EunioaException
import com.lomeone.eunoia.exception.ExceptionCategory
import com.lomeone.eunoia.exception.ExceptionDetail

private const val MESSAGE = "Deletion request already exists"
private val ERROR_CODE = ErrorCode(
    code = "user/deletion-request-already-exists",
    exceptionCategory = ExceptionCategory.BAD_REQUEST
)

class DeletionRequestAlreadyExistsException(
    detail: Map<String, Any>
) : EunioaException(
    message = MESSAGE,
    errorCode = ERROR_CODE,
    detail = ExceptionDetail(detail)
)

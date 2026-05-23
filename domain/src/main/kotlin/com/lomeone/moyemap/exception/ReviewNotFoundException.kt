package com.lomeone.moyemap.exception

import com.lomeone.eunoia.exception.EunioaException
import com.lomeone.eunoia.exception.ExceptionDetail

private const val MESSAGE = "Review not found"

class ReviewNotFoundException(
    message: String = MESSAGE,
    detail: Map<String, Any> = mapOf()
) : EunioaException(
    message = message,
    errorCode = MoyemapErrorCode.REVIEW_NOT_FOUND,
    detail = ExceptionDetail(detail)
)

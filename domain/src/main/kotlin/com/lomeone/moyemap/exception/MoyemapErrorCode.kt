package com.lomeone.moyemap.exception

import com.lomeone.eunoia.exception.ErrorCode
import com.lomeone.eunoia.exception.ExceptionCategory

object MoyemapErrorCode {
    const val ERROR_CODE_PREFIX = "moyemap"

    val VENUE_NOT_FOUND = ErrorCode(
        code = "${ERROR_CODE_PREFIX}/venue-not-found",
        exceptionCategory = ExceptionCategory.NOT_FOUND
    )

    val REVIEW_NOT_FOUND = ErrorCode(
        code = "${ERROR_CODE_PREFIX}/review-not-found",
        exceptionCategory = ExceptionCategory.NOT_FOUND
    )

    val VENUE_INVALID_STATUS = ErrorCode(
        code = "${ERROR_CODE_PREFIX}/venue-invalid-status",
        exceptionCategory = ExceptionCategory.BAD_REQUEST
    )

    val INVALID_RATING = ErrorCode(
        code = "${ERROR_CODE_PREFIX}/invalid-rating",
        exceptionCategory = ExceptionCategory.BAD_REQUEST
    )

    val INVALID_PRICE = ErrorCode(
        code = "${ERROR_CODE_PREFIX}/invalid-price",
        exceptionCategory = ExceptionCategory.BAD_REQUEST
    )

    val INVALID_LOCATION = ErrorCode(
        code = "${ERROR_CODE_PREFIX}/invalid-location",
        exceptionCategory = ExceptionCategory.BAD_REQUEST
    )
}

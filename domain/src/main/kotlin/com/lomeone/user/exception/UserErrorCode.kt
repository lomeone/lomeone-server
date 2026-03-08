package com.lomeone.user.exception

import com.lomeone.eunoia.exception.ErrorCode
import com.lomeone.eunoia.exception.ExceptionCategory

object UserErrorCode {
    const val ERROR_CODE_PREFIX = "user"
    val NOT_FOUND = ErrorCode(
        code = "${ERROR_CODE_PREFIX}/not-found",
        exceptionCategory = ExceptionCategory.NOT_FOUND
    )
    val EMAIL_ALREADY_EXISTS = ErrorCode(
        code = "${ERROR_CODE_PREFIX}/email-already-exists",
        exceptionCategory = ExceptionCategory.BAD_REQUEST
    )
    val NAME_INVALID = ErrorCode(
        code = "${ERROR_CODE_PREFIX}/name-invalid",
        exceptionCategory = ExceptionCategory.BAD_REQUEST
    )
    val NICKNAME_INVALID = ErrorCode(
        code = "${ERROR_CODE_PREFIX}/nickname-invalid",
        exceptionCategory = ExceptionCategory.BAD_REQUEST
    )
    val PHONE_NUMBER_INVALID = ErrorCode(
        code = "${ERROR_CODE_PREFIX}/phone-number-invalid",
        exceptionCategory = ExceptionCategory.BAD_REQUEST
    )
    val PHONE_NUMBER_ALREADY_EXISTS = ErrorCode(
        code = "${ERROR_CODE_PREFIX}/phone-number-already-exists",
        exceptionCategory = ExceptionCategory.BAD_REQUEST
    )
    val ROLE_EMPTY = ErrorCode(
        code = "${ERROR_CODE_PREFIX}/role-empty",
        exceptionCategory = ExceptionCategory.BAD_REQUEST
    )
}

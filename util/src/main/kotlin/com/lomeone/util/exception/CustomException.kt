package com.lomeone.util.exception

open class CustomException(
    val errorCode: String,
    message: String,
    val exceptionCategory: ExceptionCategory,
    val detail: Map<String, Any> = mapOf(),
    cause: Throwable? = null
) : RuntimeException(message, cause)

enum class ExceptionCategory {
    BAD_REQUEST,
    UNAUTHORIZED,
    FORBIDDEN,
    NOT_FOUND,
}

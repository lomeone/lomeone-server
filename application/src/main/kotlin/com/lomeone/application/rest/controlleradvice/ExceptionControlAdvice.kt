package com.lomeone.application.rest.controlleradvice

import com.lomeone.util.exception.CustomException
import com.lomeone.util.exception.ExceptionCategory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.time.ZonedDateTime

@RestControllerAdvice
class ExceptionControlAdvice {
    @ExceptionHandler(CustomException::class)
    fun handler(e: CustomException): ResponseEntity<ExceptionResponse> {
        val response = ExceptionResponse(
            status = convertErrorCategoryToHttpStatusCode(e.exceptionCategory).value(),
            errorCode = e.errorCode,
            message = e.message,
            detail = e.detail
        )

        return ResponseEntity.status(convertErrorCategoryToHttpStatusCode(e.exceptionCategory)).body(response)
    }

    private fun convertErrorCategoryToHttpStatusCode(exceptionCategory: ExceptionCategory): HttpStatus =
        when(exceptionCategory) {
            ExceptionCategory.BAD_REQUEST -> HttpStatus.BAD_REQUEST
            ExceptionCategory.UNAUTHORIZED -> HttpStatus.UNAUTHORIZED
            ExceptionCategory.FORBIDDEN -> HttpStatus.FORBIDDEN
            ExceptionCategory.NOT_FOUND -> HttpStatus.NOT_FOUND
        }
}

data class ExceptionResponse(
    val timestamp: ZonedDateTime = ZonedDateTime.now(),
    val status: Int,
    val errorCode: String,
    val message: String? = null,
    val detail: Map<String, Any> = mapOf()
)

package com.lomeone.application.graphql.handler

import com.lomeone.util.exception.CustomException
import com.lomeone.util.exception.ExceptionCategory
import com.netflix.graphql.types.errors.ErrorType
import com.netflix.graphql.types.errors.TypedGraphQLError
import graphql.execution.DataFetcherExceptionHandler
import graphql.execution.DataFetcherExceptionHandlerParameters
import graphql.execution.DataFetcherExceptionHandlerResult
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import java.util.concurrent.CompletableFuture

@Component
class DgsExceptionHandler : DataFetcherExceptionHandler {
    override fun handleException(handlerParameters: DataFetcherExceptionHandlerParameters): CompletableFuture<DataFetcherExceptionHandlerResult> {
        return when(handlerParameters.exception) {
            is CustomException -> {
                val error = newInternalError {
                    errorType(convertExceptionCategoryToDgsErrorType((handlerParameters.exception as CustomException).exceptionCategory))
                    message(handlerParameters.exception.message)
                    path(handlerParameters.path)
                }
                CompletableFuture.completedFuture(
                    DataFetcherExceptionHandlerResult.newResult().error(error).build()
                )
            }
            else -> super.handleException(handlerParameters)
        }
    }

    private fun convertExceptionCategoryToDgsErrorType(exceptionCategory: ExceptionCategory): ErrorType =
        when(exceptionCategory) {
            ExceptionCategory.BAD_REQUEST -> ErrorType.BAD_REQUEST
            ExceptionCategory.UNAUTHORIZED -> ErrorType.UNAUTHENTICATED
            ExceptionCategory.FORBIDDEN -> ErrorType.PERMISSION_DENIED
            ExceptionCategory.NOT_FOUND -> ErrorType.NOT_FOUND
        }

    private inline fun newInternalError(block: TypedGraphQLError.Builder.() -> Unit): TypedGraphQLError {
        return TypedGraphQLError.newInternalErrorBuilder().apply(block).build()
    }
}

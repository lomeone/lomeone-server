package com.lomeone.application.graphql.handler

import com.lomeone.eunoia.exception.EunioaException
import com.lomeone.eunoia.exception.ExceptionCategory
import com.netflix.graphql.dgs.exceptions.DefaultDataFetcherExceptionHandler
import com.netflix.graphql.types.errors.ErrorType
import com.netflix.graphql.types.errors.TypedGraphQLError
import graphql.execution.DataFetcherExceptionHandlerParameters
import graphql.execution.DataFetcherExceptionHandlerResult
import org.springframework.stereotype.Component
import java.util.concurrent.CompletableFuture

@Component
class DgsExceptionHandler : DefaultDataFetcherExceptionHandler() {
    override fun handleException(handlerParameters: DataFetcherExceptionHandlerParameters): CompletableFuture<DataFetcherExceptionHandlerResult> {
        return when(handlerParameters.exception) {
            is EunioaException -> {
                val error = newInternalError {
                    errorType(convertExceptionCategoryToDgsErrorType((handlerParameters.exception as EunioaException).errorCode.exceptionCategory))
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
            ExceptionCategory.INTERNAL_SERVER_ERROR -> ErrorType.UNAVAILABLE
            ExceptionCategory.SERVICE_UNAVAILABLE -> ErrorType.UNAVAILABLE
        }

    private inline fun newInternalError(block: TypedGraphQLError.Builder.() -> Unit): TypedGraphQLError {
        return TypedGraphQLError.newInternalErrorBuilder().apply(block).build()
    }
}

package com.lomeone.texhol.store.exception

import com.lomeone.eunoia.exception.ErrorCode
import com.lomeone.eunoia.exception.ExceptionCategory

object StoreErrorCode {
    const val ERROR_CODE_PREFIX = "store"
    val STORE_NAME_ALREADY_EXIST = ErrorCode(
        code = "${ERROR_CODE_PREFIX}/store-name-already-exist",
        exceptionCategory = ExceptionCategory.BAD_REQUEST
    )
    val STORE_NOT_FOUND = ErrorCode(
        code = "${ERROR_CODE_PREFIX}/store-not-found",
        exceptionCategory = ExceptionCategory.NOT_FOUND
    )
}

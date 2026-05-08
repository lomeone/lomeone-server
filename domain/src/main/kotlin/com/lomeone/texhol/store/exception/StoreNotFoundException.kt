package com.lomeone.texhol.store.exception

import com.lomeone.eunoia.exception.EunioaException
import com.lomeone.eunoia.exception.ExceptionDetail

private const val MESSAGE = "Store not found"

class StoreNotFoundException(
    message: String = MESSAGE,
    detail: Map<String, Any> = mapOf()
) : EunioaException(
    message = message,
    errorCode = StoreErrorCode.STORE_NOT_FOUND,
    detail = ExceptionDetail(detail)
)

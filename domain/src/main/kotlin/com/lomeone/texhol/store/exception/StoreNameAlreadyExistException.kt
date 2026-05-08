package com.lomeone.texhol.store.exception

import com.lomeone.eunoia.exception.EunioaException
import com.lomeone.eunoia.exception.ExceptionDetail

private const val MESSAGE = "Store name already exist"

class StoreNameAlreadyExistException(
    message: String = MESSAGE,
    detail: Map<String, Any>
) : EunioaException(
    message = message,
    errorCode = StoreErrorCode.STORE_NAME_ALREADY_EXIST,
    detail = ExceptionDetail(detail)
)

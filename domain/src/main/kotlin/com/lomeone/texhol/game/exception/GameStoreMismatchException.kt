package com.lomeone.texhol.game.exception

import com.lomeone.eunoia.exception.EunioaException
import com.lomeone.eunoia.exception.ExceptionDetail

private const val MESSAGE = "Game does not belong to the given Store"

class GameStoreMismatchException(
    message: String = MESSAGE,
    detail: Map<String, Any> = mapOf()
) : EunioaException(
    message = message,
    errorCode = GameErrorCode.GAME_STORE_MISMATCH,
    detail = ExceptionDetail(detail)
)

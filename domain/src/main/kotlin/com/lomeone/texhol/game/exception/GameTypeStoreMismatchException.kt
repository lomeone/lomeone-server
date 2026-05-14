package com.lomeone.texhol.game.exception

import com.lomeone.eunoia.exception.EunioaException
import com.lomeone.eunoia.exception.ExceptionDetail

private const val MESSAGE = "GameType does not belong to the given Store"

class GameTypeStoreMismatchException(
    message: String = MESSAGE,
    detail: Map<String, Any> = mapOf()
) : EunioaException(
    message = message,
    errorCode = GameTypeErrorCode.GAME_TYPE_STORE_MISMATCH,
    detail = ExceptionDetail(detail)
)

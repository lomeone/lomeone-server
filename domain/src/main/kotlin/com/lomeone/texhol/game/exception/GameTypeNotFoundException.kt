package com.lomeone.texhol.game.exception

import com.lomeone.eunoia.exception.EunioaException
import com.lomeone.eunoia.exception.ExceptionDetail

private const val MESSAGE = "GameType not found"

class GameTypeNotFoundException(
    message: String = MESSAGE,
    detail: Map<String, Any> = mapOf()
) : EunioaException(
    message = message,
    errorCode = GameTypeErrorCode.GAME_TYPE_NOT_FOUND,
    detail = ExceptionDetail(detail)
)

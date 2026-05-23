package com.lomeone.texhol.game.exception

import com.lomeone.eunoia.exception.EunioaException
import com.lomeone.eunoia.exception.ExceptionDetail

private const val MESSAGE = "Game not found"

class GameNotFoundException(
    message: String = MESSAGE,
    detail: Map<String, Any> = mapOf()
) : EunioaException(
    message = message,
    errorCode = GameErrorCode.GAME_NOT_FOUND,
    detail = ExceptionDetail(detail)
)

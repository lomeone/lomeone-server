package com.lomeone.texhol.game.exception

import com.lomeone.eunoia.exception.EunioaException
import com.lomeone.eunoia.exception.ExceptionDetail

private const val MESSAGE = "GameSession not found"

class GameSessionNotFoundException(
    message: String = MESSAGE,
    detail: Map<String, Any> = mapOf()
) : EunioaException(
    message = message,
    errorCode = GameSessionErrorCode.GAME_SESSION_NOT_FOUND,
    detail = ExceptionDetail(detail)
)

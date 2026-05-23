package com.lomeone.texhol.game.exception

import com.lomeone.eunoia.exception.EunioaException
import com.lomeone.eunoia.exception.ExceptionDetail

private const val MESSAGE = "Game session status is invalid"

class GameSessionInvalidStatusException(
    message: String = MESSAGE,
    detail: Map<String, Any> = mapOf()
) : EunioaException(
    message = message,
    errorCode = GameSessionErrorCode.GAME_SESSION_INVALID_STATUS,
    detail = ExceptionDetail(detail)
)

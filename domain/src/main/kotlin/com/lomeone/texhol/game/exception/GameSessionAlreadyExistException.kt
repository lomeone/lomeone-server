package com.lomeone.texhol.game.exception

import com.lomeone.eunoia.exception.EunioaException
import com.lomeone.eunoia.exception.ExceptionDetail

private const val MESSAGE = "GameSession already exists"

class GameSessionAlreadyExistException(
    message: String = MESSAGE,
    detail: Map<String, Any> = mapOf()
) : EunioaException(
    message = message,
    errorCode = GameSessionErrorCode.GAME_SESSION_ALREADY_EXIST,
    detail = ExceptionDetail(detail)
)

package com.lomeone.texhol.game.exception

import com.lomeone.eunoia.exception.EunioaException
import com.lomeone.eunoia.exception.ExceptionDetail

private const val MESSAGE = "Game name already exists"

class GameNameAlreadyExistException(
    message: String = MESSAGE,
    detail: Map<String, Any> = mapOf()
) : EunioaException(
    message = message,
    errorCode = GameErrorCode.GAME_NAME_ALREADY_EXIST,
    detail = ExceptionDetail(detail)
)

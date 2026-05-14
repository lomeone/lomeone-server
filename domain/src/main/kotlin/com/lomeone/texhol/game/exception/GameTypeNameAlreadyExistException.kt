package com.lomeone.texhol.game.exception

import com.lomeone.eunoia.exception.EunioaException
import com.lomeone.eunoia.exception.ExceptionDetail

private const val MESSAGE = "GameType name already exists"

class GameTypeNameAlreadyExistException(
    message: String = MESSAGE,
    detail: Map<String, Any> = mapOf()
) : EunioaException(
    message = message,
    errorCode = GameTypeErrorCode.GAME_TYPE_NAME_ALREADY_EXIST,
    detail = ExceptionDetail(detail)
)

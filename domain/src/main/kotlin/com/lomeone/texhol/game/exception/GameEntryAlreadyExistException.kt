package com.lomeone.texhol.game.exception

import com.lomeone.eunoia.exception.EunioaException
import com.lomeone.eunoia.exception.ExceptionDetail

private const val MESSAGE = "Game entry already exists"

class GameEntryAlreadyExistException(
    message: String = MESSAGE,
    detail: Map<String, Any> = mapOf()
) : EunioaException(
    message = message,
    errorCode = GameEntryErrorCode.GAME_ENTRY_ALREADY_EXIST,
    detail = ExceptionDetail(detail)
)

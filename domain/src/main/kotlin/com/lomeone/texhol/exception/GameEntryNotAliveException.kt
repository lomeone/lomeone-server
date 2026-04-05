package com.lomeone.texhol.exception

import com.lomeone.eunoia.exception.EunioaException
import com.lomeone.eunoia.exception.ExceptionDetail

private const val MESSAGE = "Game entry not alive"

class GameEntryNotAliveException(
    message: String = MESSAGE,
    detail: Map<String, Any>
) : EunioaException(
    message = message,
    errorCode = GameEntryErrorCode.GAME_ENTRY_NOT_ALIVE,
    detail = ExceptionDetail(detail)
)

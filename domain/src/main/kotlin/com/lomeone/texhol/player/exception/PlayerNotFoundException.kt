package com.lomeone.texhol.player.exception

import com.lomeone.eunoia.exception.EunioaException
import com.lomeone.eunoia.exception.ExceptionDetail

private const val MESSAGE = "Player not found"

class PlayerNotFoundException(
    message: String = MESSAGE,
    detail: Map<String, Any> = mapOf()
) : EunioaException(
    message = message,
    errorCode = PlayerErrorCode.PLAYER_NOT_FOUND,
    detail = ExceptionDetail(detail)
)

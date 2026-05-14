package com.lomeone.texhol.player.exception

import com.lomeone.eunoia.exception.EunioaException
import com.lomeone.eunoia.exception.ExceptionDetail

private const val MESSAGE = "Player nickname already exists"

class PlayerNicknameAlreadyExistException(
    message: String = MESSAGE,
    detail: Map<String, Any> = mapOf()
) : EunioaException(
    message = message,
    errorCode = PlayerErrorCode.PLAYER_NICKNAME_ALREADY_EXIST,
    detail = ExceptionDetail(detail)
)

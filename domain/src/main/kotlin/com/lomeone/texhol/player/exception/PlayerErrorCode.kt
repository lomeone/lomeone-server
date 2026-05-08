package com.lomeone.texhol.player.exception

import com.lomeone.eunoia.exception.ErrorCode
import com.lomeone.eunoia.exception.ExceptionCategory

object PlayerErrorCode {
    const val ERROR_CODE_PREFIX = "player"
    val PLAYER_NOT_FOUND = ErrorCode(
        code = "${ERROR_CODE_PREFIX}/player-not-found",
        exceptionCategory = ExceptionCategory.NOT_FOUND
    )
    val PLAYER_NICKNAME_ALREADY_EXIST = ErrorCode(
        code = "${ERROR_CODE_PREFIX}/player-nickname-already-exist",
        exceptionCategory = ExceptionCategory.BAD_REQUEST
    )
}

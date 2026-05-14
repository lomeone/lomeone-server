package com.lomeone.texhol.game.exception

import com.lomeone.eunoia.exception.ErrorCode
import com.lomeone.eunoia.exception.ExceptionCategory

object GameSessionErrorCode {
    const val ERROR_CODE_PREFIX = "gamesession"
    val GAME_SESSION_NOT_FOUND = ErrorCode(
        code = "${ERROR_CODE_PREFIX}/game-session-not-found",
        exceptionCategory = ExceptionCategory.NOT_FOUND
    )
    val GAME_SESSION_ALREADY_EXIST = ErrorCode(
        code = "${ERROR_CODE_PREFIX}/game-session-already-exist",
        exceptionCategory = ExceptionCategory.BAD_REQUEST
    )
}

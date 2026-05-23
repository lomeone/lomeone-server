package com.lomeone.texhol.game.exception

import com.lomeone.eunoia.exception.ErrorCode
import com.lomeone.eunoia.exception.ExceptionCategory

object GameErrorCode {
    const val ERROR_CODE_PREFIX = "game"
    val GAME_NOT_FOUND = ErrorCode(
        code = "${ERROR_CODE_PREFIX}/game-not-found",
        exceptionCategory = ExceptionCategory.NOT_FOUND
    )
    val GAME_NAME_ALREADY_EXIST = ErrorCode(
        code = "${ERROR_CODE_PREFIX}/game-name-already-exist",
        exceptionCategory = ExceptionCategory.BAD_REQUEST
    )
    val GAME_STORE_MISMATCH = ErrorCode(
        code = "${ERROR_CODE_PREFIX}/game-store-mismatch",
        exceptionCategory = ExceptionCategory.BAD_REQUEST
    )
}

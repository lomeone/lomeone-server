package com.lomeone.texhol.game.exception

import com.lomeone.eunoia.exception.ErrorCode
import com.lomeone.eunoia.exception.ExceptionCategory

object GameTypeErrorCode {
    const val ERROR_CODE_PREFIX = "gametype"
    val GAME_TYPE_NOT_FOUND = ErrorCode(
        code = "${ERROR_CODE_PREFIX}/game-type-not-found",
        exceptionCategory = ExceptionCategory.NOT_FOUND
    )
    val GAME_TYPE_NAME_ALREADY_EXIST = ErrorCode(
        code = "${ERROR_CODE_PREFIX}/game-type-name-already-exist",
        exceptionCategory = ExceptionCategory.BAD_REQUEST
    )
    val GAME_TYPE_STORE_MISMATCH = ErrorCode(
        code = "${ERROR_CODE_PREFIX}/game-type-store-mismatch",
        exceptionCategory = ExceptionCategory.BAD_REQUEST
    )
}

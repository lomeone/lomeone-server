package com.lomeone.texhol.game.exception

import com.lomeone.eunoia.exception.ErrorCode
import com.lomeone.eunoia.exception.ExceptionCategory

object GameEntryErrorCode {
    const val ERROR_CODE_PREFIX = "game-entry"
    val GAME_ENTRY_NOT_ALIVE = ErrorCode(
        code = "${ERROR_CODE_PREFIX}/game-entry-not-alive",
        exceptionCategory = ExceptionCategory.BAD_REQUEST
    )
    val GAME_ENTRY_NOT_FOUND = ErrorCode(
        code = "${ERROR_CODE_PREFIX}/game-entry-not-found",
        exceptionCategory = ExceptionCategory.NOT_FOUND
    )
    val GAME_ENTRY_ALREADY_EXIST = ErrorCode(
        code = "${ERROR_CODE_PREFIX}/game-entry-already-exist",
        exceptionCategory = ExceptionCategory.BAD_REQUEST
    )
}

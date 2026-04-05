package com.lomeone.texhol.exception

import com.lomeone.eunoia.exception.ErrorCode
import com.lomeone.eunoia.exception.ExceptionCategory

object GameEntryErrorCode {
    const val ERROR_CODE_PREFIX = "game-entry"
    val GAME_ENTRY_NOT_ALIVE = ErrorCode(
        code = "${ERROR_CODE_PREFIX}/game-entry-not-alive",
        exceptionCategory = ExceptionCategory.BAD_REQUEST
    )
}

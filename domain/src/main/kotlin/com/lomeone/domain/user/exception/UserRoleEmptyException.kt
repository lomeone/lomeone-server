package com.lomeone.domain.user.exception

import com.lomeone.util.exception.CustomException
import com.lomeone.util.exception.ExceptionCategory

class UserRoleEmptyException : CustomException(
    errorCode = ERROR_CODE,
    message = MESSAGE,
    exceptionCategory = ExceptionCategory.BAD_REQUEST
) {
    companion object {
        const val ERROR_CODE = "user/role-empty"
        const val MESSAGE = "User role must be not empty"
    }
}

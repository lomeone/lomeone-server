package com.lomeone.moyemap.exception

import com.lomeone.eunoia.exception.EunioaException
import com.lomeone.eunoia.exception.ExceptionDetail

private const val MESSAGE = "Venue not found"

class VenueNotFoundException(
    message: String = MESSAGE,
    detail: Map<String, Any> = mapOf()
) : EunioaException(
    message = message,
    errorCode = MoyemapErrorCode.VENUE_NOT_FOUND,
    detail = ExceptionDetail(detail)
)

package com.lomeone.application.seucirty.handler

import com.lomeone.authentication.service.JwtTokenProvider
import com.lomeone.authentication.service.REFRESH_EXPIRES_AT
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AuthenticationSuccessHandler

class EmailPasswordAuthenticationSuccessHandler(
    private val jwtTokenProvider: com.lomeone.authentication.service.JwtTokenProvider
) : AuthenticationSuccessHandler {

    private val log = LoggerFactory.getLogger(this.javaClass)

    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ) {
        val tokenInfo = jwtTokenProvider.issueToken(authentication)

        log.info("accessToken: {}, refreshToken: {}", tokenInfo.accessToken, tokenInfo.refreshToken)

        response.addHeader("Authorization", "Bearer ${tokenInfo.accessToken}")

        val refreshTokenCookie = makeCookie("refreshToken", tokenInfo.refreshToken)
        response.addCookie(refreshTokenCookie)
    }

    private fun makeCookie(name: String, value: String): Cookie {
        val cookie = Cookie(name, value)
        cookie.path = "/"
        cookie.isHttpOnly = true
        cookie.maxAge = _root_ide_package_.com.lomeone.authentication.service.REFRESH_EXPIRES_AT.toInt()
        cookie.domain = "lomeone.com"

        return cookie
    }
}

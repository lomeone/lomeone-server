package com.lomeone.application.fiilter

import com.lomeone.domain.authentication.service.ACCESS_EXPIRES_AT
import com.lomeone.domain.authentication.service.JwtTokenProvider
import com.lomeone.domain.authentication.service.REFRESH_EXPIRES_AT
import jakarta.servlet.FilterChain
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

class JwtAuthenticationFilter(
    authenticationManager: AuthenticationManager,
    private val jwtTokenProvider: JwtTokenProvider
) : UsernamePasswordAuthenticationFilter(authenticationManager) {
    override fun successfulAuthentication(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain,
        authResult: Authentication
    ) {
        val tokenInfo = jwtTokenProvider.issueToken(authResult)

        response.addHeader("Authorization", tokenInfo.accessToken)

        val accessTokenCookie = Cookie("accessToken", tokenInfo.accessToken)
        accessTokenCookie.path = "/"
        accessTokenCookie.isHttpOnly = true
        accessTokenCookie.maxAge = ACCESS_EXPIRES_AT.toInt()
        accessTokenCookie.domain = "lomeone.com"
        response.addCookie(accessTokenCookie)

        val refreshTokenCookie = Cookie("refreshToken", tokenInfo.refreshToken)
        refreshTokenCookie.path = "/"
        refreshTokenCookie.isHttpOnly = true
        refreshTokenCookie.maxAge = REFRESH_EXPIRES_AT.toInt()
        accessTokenCookie.domain = "lomeone.com"
        response.addCookie(refreshTokenCookie)

        super.successfulAuthentication(request, response, chain, authResult)
    }
}

package com.lomeone.application.fiilter

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.lomeone.domain.authentication.service.ACCESS_EXPIRES_AT
import com.lomeone.domain.authentication.service.JwtTokenProvider
import com.lomeone.domain.authentication.service.REFRESH_EXPIRES_AT
import jakarta.servlet.FilterChain
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

class JwtAuthenticationFilter(
    private val authenticationManager: AuthenticationManager,
    private val jwtTokenProvider: JwtTokenProvider
) : UsernamePasswordAuthenticationFilter(authenticationManager) {
    data class LoginRequest(
            val email: String,
            val password: String
    )

    private val log = LoggerFactory.getLogger(this.javaClass)

    // call when call '/login'
    override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse): Authentication {
        val objectMapper = ObjectMapper().registerKotlinModule()
        val loginRequest = objectMapper.readValue(request.inputStream, LoginRequest::class.java)

        log.debug("email: {}, password: {}", loginRequest.email, loginRequest.password)

        val authenticationToken = UsernamePasswordAuthenticationToken(loginRequest.email, loginRequest.password)
        return authenticationManager.authenticate(authenticationToken)
    }

    override fun successfulAuthentication(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain,
        authResult: Authentication
    ) {
        val tokenInfo = jwtTokenProvider.issueToken(authResult)

        response.addHeader("Authorization", tokenInfo.accessToken)

        log.info("accessToken: {}, refreshToken: {}", tokenInfo.accessToken, tokenInfo.refreshToken)

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
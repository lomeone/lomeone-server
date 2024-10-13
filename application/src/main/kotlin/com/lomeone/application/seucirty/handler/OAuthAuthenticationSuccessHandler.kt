package com.lomeone.application.seucirty.handler

import com.lomeone.domain.authentication.service.JwtTokenProvider
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.stereotype.Component

@Component
class OAuthAuthenticationSuccessHandler(
    private val jwtTokenProvider: JwtTokenProvider
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
    }
}

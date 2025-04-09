package com.lomeone.application.seucirty.filter

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.lomeone.application.seucirty.handler.EmailPasswordAuthenticationSuccessHandler
import com.lomeone.domain.authentication.service.JwtTokenProvider
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

class EmailPasswordAuthenticationFilter(
    private val authenticationManager: AuthenticationManager,
    jwtTokenProvider: JwtTokenProvider
) : UsernamePasswordAuthenticationFilter(authenticationManager) {

    private val log = LoggerFactory.getLogger(this.javaClass)

    init {
        setAuthenticationSuccessHandler(EmailPasswordAuthenticationSuccessHandler(jwtTokenProvider))
    }

    data class LoginRequest(
        val email: String,
        val password: String
    )

    // call when call '/login'
    override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse): Authentication {
        val objectMapper = ObjectMapper().registerKotlinModule()
        val loginRequest = objectMapper.readValue(request.inputStream, LoginRequest::class.java)

        log.debug("email: {}, password: {}", loginRequest.email, loginRequest.password)

        val authenticationToken = UsernamePasswordAuthenticationToken(loginRequest.email, loginRequest.password)
        return authenticationManager.authenticate(authenticationToken)
    }
}

package com.lomeone.domain.authentication.service

import io.kotest.core.spec.style.FreeSpec
import io.mockk.every
import io.mockk.mockk
import org.springframework.security.core.Authentication

class JwtTokenProviderTest : FreeSpec({
    val jwtTokenProvider = JwtTokenProvider("ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyz")

    "인증 정보를 통해서 jwt token을 발급할 수 있다" - {
        val authentication: Authentication = mockk()
        every { authentication.name } returns "authentication-uid"

        val token = jwtTokenProvider.issueToken(authentication)

        println(token.accessToken)
        println(token.refreshToken)
        println(token.expiresIn)
    }
})

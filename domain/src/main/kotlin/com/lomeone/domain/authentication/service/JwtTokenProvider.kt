package com.lomeone.domain.authentication.service

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import java.util.Date

const val ACCESS_EXPIRES_AT: Long = 1000 * 60 * 30          // 30 minute
const val REFRESH_EXPIRES_AT: Long = 1000 * 60 * 60 * 12    // 12 hour

@Service
class JwtTokenProvider {
    @Value("\${jwt.secret}")
    private lateinit var jwtSecret: String

    private val key by lazy { Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret)) }

    fun issueToken(authentication: Authentication): TokenInfo {
        val now = Date()

        val accessToken = Jwts.builder()
            .subject(authentication.name)
            .issuedAt(now)
            .expiration(Date(now.time + ACCESS_EXPIRES_AT))
            .signWith(key, Jwts.SIG.HS256)
            .compact()

        val refreshToken = Jwts.builder()
            .expiration(Date(now.time + REFRESH_EXPIRES_AT))
            .signWith(key, Jwts.SIG.HS256)
            .compact()

        return TokenInfo(
            accessToken = accessToken,
            refreshToken = refreshToken,
            expiresIn = ACCESS_EXPIRES_AT.toInt()
        )
    }
}

data class TokenInfo(
    private val accessToken: String,
    private val refreshToken: String,
    private val expiresIn: Int
)

package com.lomeone.authentication.service

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
class JwtTokenProvider(
    @Value("\${jwt.secret}")
    private val jwtSecret: String
) {
    private val key by lazy { Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret)) }

    fun issueToken(authentication: Authentication): com.lomeone.authentication.service.TokenInfo {
        val now = Date()

        val accessToken = Jwts.builder()
            .subject(authentication.name)
            .issuedAt(now)
            .expiration(Date(now.time + _root_ide_package_.com.lomeone.authentication.service.ACCESS_EXPIRES_AT))
            .signWith(key, Jwts.SIG.HS256)
            .compact()

        val refreshToken = Jwts.builder()
            .subject(authentication.name)
            .issuedAt(now)
            .expiration(Date(now.time + _root_ide_package_.com.lomeone.authentication.service.REFRESH_EXPIRES_AT))
            .signWith(key, Jwts.SIG.HS256)
            .compact()

        return _root_ide_package_.com.lomeone.authentication.service.TokenInfo(
            accessToken = accessToken,
            refreshToken = refreshToken,
            expiresIn = _root_ide_package_.com.lomeone.authentication.service.ACCESS_EXPIRES_AT.toInt()
        )
    }

    fun getSubject(token: String): String =
        Jwts.parser().verifyWith(key).build().parseSignedClaims(token).payload.subject
}

data class TokenInfo(
    val accessToken: String,
    val refreshToken: String,
    val expiresIn: Int
)

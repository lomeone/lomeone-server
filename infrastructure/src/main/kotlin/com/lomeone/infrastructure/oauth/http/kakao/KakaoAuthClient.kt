package com.lomeone.infrastructure.oauth.http.kakao

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@FeignClient(name = "kakao-auth", url = "\${oauth.kakao.url.auth}")
interface KakaoAuthClient {
    @PostMapping("/oauth/token")
    fun getKakaoToken(@RequestBody request: KakaoTokensRequest): KakaoTokens
}

data class KakaoTokensRequest(
    val grantType: String,
    val clientId: String,
    val redirectUri: String,
    val code: String
)

data class KakaoTokens(
    val tokenType: String,
    val accessToken: String,
    val expiresIn: Int,
    val refreshToken: String,
    val refreshTokenExpiresIn: Int
)

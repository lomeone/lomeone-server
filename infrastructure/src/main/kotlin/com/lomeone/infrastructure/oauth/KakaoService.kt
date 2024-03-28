package com.lomeone.infrastructure.oauth

import com.lomeone.domain.authentication.entity.AuthProvider
import com.lomeone.domain.authentication.handler.OAuthAuthenticationHandler
import com.lomeone.domain.authentication.handler.OAuthTokensRequest
import com.lomeone.domain.authentication.handler.OAuthTokensResponse
import com.lomeone.domain.authentication.handler.OAuthUserDataRequest
import com.lomeone.domain.authentication.handler.OAuthUserDataResponse
import com.lomeone.infrastructure.oauth.http.kakao.KakaoApiClient
import com.lomeone.infrastructure.oauth.http.kakao.KakaoAuthClient
import com.lomeone.infrastructure.oauth.http.kakao.KakaoTokensRequest
import org.springframework.beans.factory.annotation.Value

const val GRANT_TYPE = "authorization_code"

class KakaoService(
    private val kakaoAuthClient: KakaoAuthClient,
    private val kakaoApiClient: KakaoApiClient
): OAuthAuthenticationHandler {
    @Value("\${oauth.kakao.client-id}")
    private lateinit var clientId: String

    @Value("\${oauth.kakao.redirect-uri")
    private lateinit var redirectUri: String

    override fun getOAuthProvider(): AuthProvider = AuthProvider.KAKAO

    override fun getOAuthToken(request: OAuthTokensRequest): OAuthTokensResponse {

        val kakaoTokensRequest = KakaoTokensRequest(
            grantType = GRANT_TYPE,
            clientId = this.clientId,
            redirectUri = this.redirectUri,
            code = request.code
        )
        val kakaoTokens = kakaoAuthClient.getKakaoToken(kakaoTokensRequest)

        return OAuthTokensResponse(
            accessToken = kakaoTokens.accessToken,
            refreshToken = kakaoTokens.refreshToken,
            expiresIn = kakaoTokens.expiresIn
        )
    }

    override fun getOAuthUserData(request: OAuthUserDataRequest): OAuthUserDataResponse {
        TODO("Not yet implemented")
    }
}

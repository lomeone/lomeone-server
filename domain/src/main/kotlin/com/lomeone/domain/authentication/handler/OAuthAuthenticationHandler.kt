package com.lomeone.domain.authentication.handler

import com.lomeone.domain.authentication.entity.AuthProvider

interface OAuthAuthenticationHandler {
    fun getOAuthProvider(): AuthProvider
    fun getOAuthToken(request: OAuthTokensRequest): OAuthTokensResponse
    fun getOAuthUserData(request: OAuthUserDataRequest): OAuthUserDataResponse
}

data class OAuthTokensRequest(
    val code: String,
    val state: String,
    val anotherRequiredData: Map<String, String> = mapOf()
)

data class OAuthTokensResponse(
    val accessToken: String,
    val refreshToken: String,
    val expiresIn: Int
)

data class OAuthUserDataRequest(
    val accessToken: String
)

data class OAuthUserDataResponse(
    val providerUid: String,
    val email: String,
    val phoneNumber: String
)

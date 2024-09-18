package com.lomeone.domain.authentication.service

import com.lomeone.domain.authentication.repository.AuthenticationRepository
import com.lomeone.domain.user.service.GetUserByUserTokenService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RegisterOAuthAuthenticationService(
    private val authenticationRepository: AuthenticationRepository,
    private val getUserByUserTokenService: GetUserByUserTokenService
) : OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    @Transactional
    override fun loadUser(userRequest: OAuth2UserRequest?): OAuth2User {
        TODO("Not yet implemented")
    }

}

enum class AuthRegisterProvider(val value: String) {
    EMAIL("email-register"),
    GOOGLE("google-register"),
    FACEBOOK("facebook-register"),
    APPLE("apple-register"),
    GITHUB("github-register"),
    KAKAO("kakao-register"),
    NAVER("naver-register"),
    LINE("line-register")
}

package com.lomeone.authentication.service

import com.lomeone.authentication.repository.AuthenticationRepository
import com.lomeone.user.service.GetUserByUserToken
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RegisterOAuthAuthenticationService(
    private val authenticationRepository: com.lomeone.authentication.repository.AuthenticationRepository,
    private val getUserByUserTokenService: com.lomeone.user.service.GetUserByUserToken
) : OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    @Transactional
    override fun loadUser(userRequest: OAuth2UserRequest?): OAuth2User {
        TODO("Not yet implemented")
    }

}

enum class AuthRegisterProvider(val value: String) {
    GOOGLE("google-register"),
    FACEBOOK("facebook-register"),
    APPLE("apple-register"),
    GITHUB("github-register"),
    KAKAO("kakao-register"),
    NAVER("naver-register"),
    LINE("line-register")
}

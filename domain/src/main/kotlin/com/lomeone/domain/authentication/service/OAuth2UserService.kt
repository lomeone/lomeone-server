package com.lomeone.domain.authentication.service

import com.lomeone.domain.authentication.entity.AuthProvider
import com.lomeone.domain.authentication.entity.Authentication
import com.lomeone.domain.authentication.exception.AuthenticationNotFoundException
import com.lomeone.domain.authentication.exception.OAuth2ProviderNotSupportedException
import com.lomeone.domain.authentication.repository.AuthenticationRepository
import com.lomeone.domain.user.service.CreateUserCommand
import com.lomeone.domain.user.service.CreateUserResult
import com.lomeone.domain.user.service.CreateUserService
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class OAuth2UserService(
    private val authenticationRepository: AuthenticationRepository,
    private val createUserService: CreateUserService,
) : OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    @Transactional
    override fun loadUser(userRequest: OAuth2UserRequest): OAuth2User {
        val delegate = DefaultOAuth2UserService()
        val oAuth2User = delegate.loadUser(userRequest)

        val registrationId = userRequest.clientRegistration.registrationId
        val uid = registrationId + oAuth2User.name
        val attributes = oAuth2User.attributes

        return authenticationRepository.findByUid(uid)
            ?.let { authentication ->
                authentication.signIn()
                OAuthUser(authentication, attributes)
            }
            ?: run {
                val authentication = createNewAuthentication(registrationId, uid, attributes)
                OAuthUser(authentication, attributes)
            }
    }

    private fun createNewAuthentication(registrationId: String, uid: String, attributes: MutableMap<String, Any>): Authentication {
        val userInfo = getUserInfo(registrationId, attributes)

        val result = createUser(userInfo, uid)

        return authenticationRepository.findByUid(uid) ?: throw AuthenticationNotFoundException(mapOf("uid" to uid))
    }

    private fun getUserInfo(registrationId: String, attributes: MutableMap<String, Any>): OAuth2UserInfo =
        when(registrationId) {
            AuthProvider.KAKAO.value -> KakaoUserInfo(attributes)
            else -> throw OAuth2ProviderNotSupportedException(mapOf("oauth2" to registrationId))
        }

    private fun createUser(userInfo: OAuth2UserInfo, uid: String): CreateUserResult {
        val command = CreateUserCommand(
            userInfo = CreateUserCommand.UserInfo(
                name = userInfo.getName(),
                nickname = userInfo.getNickname(),
                email =  userInfo.getEmail(),
                phoneNumber = userInfo.getPhoneNumber(),
                birthday = userInfo.getBirthday()
            ),
            authenticationInfo = CreateUserCommand.AuthenticationInfo(
                email = userInfo.getEmail(),
                provider = userInfo.getProvider(),
                uid = uid
            )
        )

        return createUserService.createUser(command)
    }
}


data class OAuthUser(
    private val authentication: Authentication,
    private val attributes: MutableMap<String, Any>
) : OAuth2User {
    override fun getName(): String = authentication.uid

    override fun getAttributes(): MutableMap<String, Any> = attributes

    override fun getAuthorities(): List<GrantedAuthority> =
        authentication.user.userRoles.map { SimpleGrantedAuthority("ROLE_${it.role.roleName}") }
}

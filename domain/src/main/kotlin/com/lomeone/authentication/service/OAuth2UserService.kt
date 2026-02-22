package com.lomeone.authentication.service

import com.lomeone.authentication.entity.AuthProvider
import com.lomeone.authentication.entity.Authentication
import com.lomeone.authentication.exception.AuthenticationNotFoundException
import com.lomeone.authentication.exception.OAuth2ProviderNotSupportedException
import com.lomeone.authentication.repository.AuthenticationRepository
import com.lomeone.user.service.CreateUserCommand
import com.lomeone.user.service.CreateUserResult
import com.lomeone.user.service.CreateUser
import com.lomeone.user.service.GetUserByUserToken
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
    private val authenticationRepository: com.lomeone.authentication.repository.AuthenticationRepository,
    private val createUserService: com.lomeone.user.service.CreateUser,
    private val getUserByUserTokenService: com.lomeone.user.service.GetUserByUserToken,
    private val registerAuthenticationService: com.lomeone.authentication.service.RegisterAuthentication
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
                _root_ide_package_.com.lomeone.authentication.service.OAuthUser(authentication, attributes)
            }
            ?: run {
                val authentication = createNewAuthentication(registrationId, uid, attributes)
                _root_ide_package_.com.lomeone.authentication.service.OAuthUser(authentication, attributes)
            }
    }

    private fun createNewAuthentication(registrationId: String, uid: String, attributes: MutableMap<String, Any>): com.lomeone.authentication.entity.Authentication {
        val oAuthUserInfo = getOAuthUserInfo(registrationId, attributes)

        val realmCode = attributes["realm_code"]

        realmCode == null && throw IllegalArgumentException("Realm code is required for authentication")

        registerAuthenticationService.execute(
            _root_ide_package_.com.lomeone.authentication.service.RegisterAuthenticationCommand(
                email = oAuthUserInfo.getEmail(),
                provider = oAuthUserInfo.getProvider(),
                uid = uid,
                realmCode = realmCode as String
            )
        )

        return authenticationRepository.findByUid(uid) ?: throw _root_ide_package_.com.lomeone.authentication.exception.AuthenticationNotFoundException(
            mapOf("uid" to uid)
        )
    }

    private fun getOAuthUserInfo(registrationId: String, attributes: MutableMap<String, Any>): com.lomeone.authentication.service.OAuth2UserInfo =
        when(registrationId) {
            _root_ide_package_.com.lomeone.authentication.entity.AuthProvider.KAKAO.value -> _root_ide_package_.com.lomeone.authentication.service.KakaoUserInfo(
                attributes
            )
            else -> throw _root_ide_package_.com.lomeone.authentication.exception.OAuth2ProviderNotSupportedException(
                mapOf("oauth2" to registrationId)
            )
        }

    private fun createUser(userInfo: com.lomeone.authentication.service.OAuth2UserInfo, uid: String): com.lomeone.user.service.CreateUserResult {
        val command = _root_ide_package_.com.lomeone.user.service.CreateUserCommand(
            name = userInfo.getName(),
            nickname = userInfo.getNickname(),
            email = userInfo.getEmail(),
            phoneNumber = userInfo.getPhoneNumber(),
            birthday = userInfo.getBirthday(),
            authenticationUid = uid,
            realmCode = "userInfo.getRealmCode()"
        )

        return createUserService.execute(command)
    }
}


data class OAuthUser(
    private val authentication: com.lomeone.authentication.entity.Authentication,
    private val attributes: MutableMap<String, Any>
) : OAuth2User {
    override fun getName(): String = authentication.uid

    override fun getAttributes(): MutableMap<String, Any> = attributes

    override fun getAuthorities(): List<GrantedAuthority> =
        authentication.user?.let {
            it.userRoles.map { SimpleGrantedAuthority("ROLE_${it.role.roleName}") }
        } ?: emptyList()
}

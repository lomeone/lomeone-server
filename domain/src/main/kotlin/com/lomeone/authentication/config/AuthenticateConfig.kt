package com.lomeone.authentication.config

import com.lomeone.authentication.entity.AuthProvider
import com.lomeone.authentication.entity.Authentication
import com.lomeone.authentication.exception.AuthenticationNotFoundException
import com.lomeone.authentication.repository.AuthenticationRepository
import com.lomeone.authentication.exception.RealmNotFoundException
import com.lomeone.authentication.repository.RealmRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.transaction.annotation.Transactional

typealias RealmUserDetailsLoader = (realmCode: String, username: String) -> UserDetails

@Configuration
class AuthenticateConfig {
    @Bean
    fun bcryptEncoder(): BCryptPasswordEncoder = BCryptPasswordEncoder()

    @Bean
    @Transactional
    fun loadUserByRealmAndUsername(
        realmRepository: com.lomeone.authentication.repository.RealmRepository,
        authenticationRepository: com.lomeone.authentication.repository.AuthenticationRepository
    ): com.lomeone.authentication.config.RealmUserDetailsLoader = { realmCode, username ->
        val realm = realmRepository.findByCode(realmCode)
            ?: throw _root_ide_package_.com.lomeone.authentication.exception.RealmNotFoundException(mapOf("realmCode" to realmCode))

        val authentication = authenticationRepository.findByEmailAndProviderAndRealm(
            email = username,
            provider = _root_ide_package_.com.lomeone.authentication.entity.AuthProvider.EMAIL,
            realm = realm
        ) ?: throw _root_ide_package_.com.lomeone.authentication.exception.AuthenticationNotFoundException(
            mapOf(
                "email" to username,
                "provider" to _root_ide_package_.com.lomeone.authentication.entity.AuthProvider.EMAIL,
                "realmCode" to realmCode
            )
        )

        authentication.signIn()
        _root_ide_package_.com.lomeone.authentication.config.PrincipalDetails(authentication)
    }
}

data class PrincipalDetails(
    private val authentication: com.lomeone.authentication.entity.Authentication
) : UserDetails {

    override fun getAuthorities(): List<GrantedAuthority> =
        authentication.user?.let {
            it.userRoles.map { SimpleGrantedAuthority("ROLE_${it.role.roleName}") }
        } ?: emptyList()

    override fun getPassword(): String? = authentication.password

    override fun getUsername(): String = authentication.uid

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = true
}

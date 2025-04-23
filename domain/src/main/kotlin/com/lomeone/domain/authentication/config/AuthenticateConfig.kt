package com.lomeone.domain.authentication.config

import com.lomeone.domain.authentication.entity.AuthProvider
import com.lomeone.domain.authentication.entity.Authentication
import com.lomeone.domain.authentication.exception.AuthenticationNotFoundException
import com.lomeone.domain.authentication.repository.AuthenticationRepository
import com.lomeone.domain.realm.exception.RealmNotFoundException
import com.lomeone.domain.realm.repository.RealmRepository
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
        realmRepository: RealmRepository,
        authenticationRepository: AuthenticationRepository
    ): RealmUserDetailsLoader = { realmCode, username ->
        val realm = realmRepository.findByCode(realmCode)
            ?: throw RealmNotFoundException(mapOf("realmCode" to realmCode))

        val authentication = authenticationRepository.findByEmailAndProviderAndRealm(
            email = username,
            provider = AuthProvider.EMAIL,
            realm = realm
        ) ?: throw AuthenticationNotFoundException(
            mapOf(
                "email" to username,
                "provider" to AuthProvider.EMAIL,
                "realmCode" to realmCode
            )
        )

        authentication.signIn()
        PrincipalDetails(authentication)
    }
}

data class PrincipalDetails(
    private val authentication: Authentication
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

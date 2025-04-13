package com.lomeone.domain.authentication.service

import com.lomeone.domain.authentication.entity.AuthProvider
import com.lomeone.domain.authentication.entity.Authentication
import com.lomeone.domain.authentication.exception.AuthenticationNotFoundException
import com.lomeone.domain.authentication.repository.AuthenticationRepository
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PrincipalDetailService(
    private val authenticationRepository: AuthenticationRepository
): UserDetailsService {
    @Transactional
    override fun loadUserByUsername(username: String): UserDetails =
        authenticationRepository.findByEmailAndProvider(email = username, provider = AuthProvider.EMAIL)
            ?.let {authentication ->
                authentication.signIn()
                PrincipalDetails(authentication)
            }
            ?: throw AuthenticationNotFoundException(mapOf("id" to username))
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

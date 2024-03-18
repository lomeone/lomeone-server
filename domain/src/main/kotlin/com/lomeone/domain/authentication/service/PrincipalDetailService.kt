package com.lomeone.domain.authentication.service

import com.lomeone.domain.authentication.entity.AuthProvider
import com.lomeone.domain.authentication.entity.Authentication
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
    @Transactional(readOnly = true)
    override fun loadUserByUsername(username: String): UserDetails =
        authenticationRepository.findByEmailAndProvider(email = username, provider = AuthProvider.EMAIL)
            ?.let { PrincipalDetails(it) }
            ?: throw Exception("Authentication does not exist")
}

data class PrincipalDetails(
    private val authentication: Authentication
): UserDetails {

    override fun getAuthorities(): List<GrantedAuthority> =
        authentication.user.userRoles.map { SimpleGrantedAuthority("ROLE_${it.role.roleName}") }

    override fun getPassword(): String? = authentication.password

    override fun getUsername(): String = authentication.uid

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = true
}

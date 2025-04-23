package com.lomeone.application.seucirty.provider

import com.lomeone.domain.authentication.config.RealmUserDetailsLoader
import org.springframework.security.authentication.AccountStatusUserDetailsChecker
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.SpringSecurityMessageSource
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class RealmAuthenticationProvider(
    private val loadUserByRealmAndUsername: RealmUserDetailsLoader,
    private val passwordEncoder: PasswordEncoder
) : AuthenticationProvider {

    val messages = SpringSecurityMessageSource.getAccessor();

    private val userDetailsChecker = AccountStatusUserDetailsChecker()
    private val authorityMapper = SimpleAuthorityMapper()

    override fun authenticate(authentication: Authentication): Authentication {
        val token = authentication as RealmUsernamePasswordAuthenticationToken

        val realmCode = token.realmCode
        val username = determineUserName(token)

        val userDetails = loadUserByRealmAndUsername(realmCode, username)

        userDetailsChecker.check(userDetails)

        authenticationCheck(userDetails, token)

        return createSuccessAuthentication(userDetails, authentication, userDetails)
    }

    private fun determineUserName(authentication: Authentication): String =
        if (authentication.principal != null) authentication.name else "NONE_PROVIDED"

    private fun authenticationCheck(userDetails: UserDetails, authentication: RealmUsernamePasswordAuthenticationToken) {
        authentication.credentials == null && throw BadCredentialsException(
            messages.getMessage(
                "RealmAuthenticationProvider.badCredentials",
                "Bad credentials"
            )
        )

        val presentedPassword = authentication.credentials.toString()
        passwordEncoder.matches(presentedPassword, userDetails.password) && throw BadCredentialsException(
            messages.getMessage(
                "RealmAuthenticationProvider.badCredentials",
                "Bad credentials"

            )
        )
    }

    private fun createSuccessAuthentication(principal: Any, authentication: Authentication, userDetails: UserDetails): Authentication {
        val result = UsernamePasswordAuthenticationToken(principal, authentication.credentials, this.authorityMapper.mapAuthorities(userDetails.authorities))
        result.details = authentication.details
        return result
    }

    override fun supports(authentication: Class<*>): Boolean =
        RealmUsernamePasswordAuthenticationToken::class.java.isAssignableFrom(authentication)
}

class RealmUsernamePasswordAuthenticationToken(
    val realmCode: String,
    principal: Any,
    credentials: Any
) : UsernamePasswordAuthenticationToken(principal, credentials)

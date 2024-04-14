package com.lomeone.domain.authentication.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@Configuration
class AuthenticateConfig(
    private val authenticationConfiguration: AuthenticationConfiguration
) {

    @Bean
    fun authenticationManager(): AuthenticationManager = authenticationConfiguration.authenticationManager

    @Bean
    fun bcryptEncoder(): BCryptPasswordEncoder = BCryptPasswordEncoder()
}
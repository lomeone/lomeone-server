package com.lomeone.domain.authentication.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@Configuration
class AuthenticateConfig {
    @Bean
    fun bcryptEncoder(): BCryptPasswordEncoder = BCryptPasswordEncoder()
}
package com.lomeone.application.seucirty.config

import com.lomeone.application.seucirty.filter.EmailPasswordAuthenticationFilter
import com.lomeone.domain.authentication.service.JwtTokenProvider
import com.lomeone.domain.authentication.service.OAuth2UserService
import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.Authentication
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val oAuth2UserService: OAuth2UserService
) {
    @Bean
    fun filterChain(
        http: HttpSecurity,
        authenticationConfiguration: AuthenticationConfiguration,
        jwtTokenProvider: JwtTokenProvider
    ): SecurityFilterChain {
        http.httpBasic { it.disable() }
            .csrf { it.disable() }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .formLogin { it.disable() }
            .authorizeHttpRequests {
                it.anyRequest().permitAll()
            }
            .addFilter(EmailPasswordAuthenticationFilter(
                authenticationManager = authenticationConfiguration.authenticationManager,
                jwtTokenProvider = jwtTokenProvider
            ))
            .oauth2Login {
                it.userInfoEndpoint {
                    it.userService(oAuth2UserService)
                }
                it.successHandler { request, response, authentication ->
                    val tokenInfo = jwtTokenProvider.issueToken(authentication)
                    response.addHeader("Authorization", "Bearer ${tokenInfo.accessToken}")
                }
                it.failureHandler { request, response, exception ->
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, exception.message)
                }
            }

        return http.build()
    }
}

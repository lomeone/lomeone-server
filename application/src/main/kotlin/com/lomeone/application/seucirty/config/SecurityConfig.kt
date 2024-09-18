package com.lomeone.application.seucirty.config

import com.lomeone.application.seucirty.filter.EmailPasswordAuthenticationFilter
import com.lomeone.application.seucirty.handler.OAuthAuthenticationSuccessHandler
import com.lomeone.domain.authentication.service.JwtTokenProvider
import com.lomeone.domain.authentication.service.OAuth2UserService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val oAuth2UserService: OAuth2UserService,
    private val oAuthAuthenticationSuccessHandler: OAuthAuthenticationSuccessHandler
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
            .oauth2Login { oAuth2LoginConfigure ->
                oAuth2LoginConfigure
                    .userInfoEndpoint {
                        it.userService(oAuth2UserService)
                    }
                    .successHandler(oAuthAuthenticationSuccessHandler)
            }

        return http.build()
    }
}

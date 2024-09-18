package com.lomeone.application.seucirty.config

import com.lomeone.application.seucirty.filter.EmailPasswordAuthenticationFilter
import com.lomeone.application.seucirty.handler.OAuthAuthenticationSuccessHandler
import com.lomeone.domain.authentication.entity.AuthProvider
import com.lomeone.domain.authentication.exception.OAuth2ProviderNotSupportedException
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
    private val oAuth2UserService: OAuth2UserService<Any?, Any?>,
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
                    .userInfoEndpoint { userInfoEndpointConfig ->
                        userInfoEndpointConfig
                            .userService { userRequest ->
                                when (val registrationId = userRequest.clientRegistration.registrationId) {
                                    AuthProvider.GOOGLE.value,
                                    AuthProvider.FACEBOOK.value,
                                    AuthProvider.APPLE.value,
                                    AuthProvider.GITHUB.value,
                                    AuthProvider.KAKAO.value,
                                    AuthProvider.NAVER.value,
                                    AuthProvider.LINE.value -> oAuth2UserService.loadUser(userRequest)
                                    AuthRegisterProvider.GOOGLE.value,
                                    AuthRegisterProvider.FACEBOOK.value,
                                    AuthRegisterProvider.APPLE.value,
                                    AuthRegisterProvider.GITHUB.value,
                                    AuthRegisterProvider.KAKAO.value,
                                    AuthRegisterProvider.NAVER.value,
                                    AuthRegisterProvider.LINE.value -> TODO("Not yet implemented")
                                    else -> throw OAuth2ProviderNotSupportedException(mapOf("oauth2" to registrationId))
                                }
                            }
                    }
                    .successHandler(oAuthAuthenticationSuccessHandler)
            }

        return http.build()
    }
}

enum class AuthRegisterProvider(val value: String) {
    EMAIL("email-register"),
    GOOGLE("google-register"),
    FACEBOOK("facebook-register"),
    APPLE("apple-register"),
    GITHUB("github-register"),
    KAKAO("kakao-register"),
    NAVER("naver-register"),
    LINE("line-register")
}

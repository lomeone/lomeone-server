package com.lomeone.application.seucirty.config

import com.lomeone.application.seucirty.filter.EmailPasswordAuthenticationFilter
import com.lomeone.application.seucirty.handler.OAuthAuthenticationSuccessHandler
import com.lomeone.authentication.entity.AuthProvider
import com.lomeone.authentication.exception.OAuth2ProviderNotSupportedException
import com.lomeone.authentication.service.AuthRegisterProvider
import com.lomeone.authentication.service.JwtTokenProvider
import com.lomeone.authentication.service.OAuth2UserService
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
    private val oAuth2UserService: com.lomeone.authentication.service.OAuth2UserService,
    private val oAuthAuthenticationSuccessHandler: OAuthAuthenticationSuccessHandler
) {
    @Bean
    fun filterChain(
        http: HttpSecurity,
        authenticationConfiguration: AuthenticationConfiguration,
        jwtTokenProvider: com.lomeone.authentication.service.JwtTokenProvider
    ): SecurityFilterChain {
        http.httpBasic { it.disable() }
            .csrf { it.disable() }
            .headers { it.frameOptions { it.disable() } }  // for h2-console
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
                                    _root_ide_package_.com.lomeone.authentication.entity.AuthProvider.GOOGLE.value,
                                    _root_ide_package_.com.lomeone.authentication.entity.AuthProvider.FACEBOOK.value,
                                    _root_ide_package_.com.lomeone.authentication.entity.AuthProvider.APPLE.value,
                                    _root_ide_package_.com.lomeone.authentication.entity.AuthProvider.GITHUB.value,
                                    _root_ide_package_.com.lomeone.authentication.entity.AuthProvider.KAKAO.value,
                                    _root_ide_package_.com.lomeone.authentication.entity.AuthProvider.NAVER.value,
                                    _root_ide_package_.com.lomeone.authentication.entity.AuthProvider.LINE.value -> oAuth2UserService.loadUser(userRequest)
                                    _root_ide_package_.com.lomeone.authentication.service.AuthRegisterProvider.GOOGLE.value,
                                    _root_ide_package_.com.lomeone.authentication.service.AuthRegisterProvider.FACEBOOK.value,
                                    _root_ide_package_.com.lomeone.authentication.service.AuthRegisterProvider.APPLE.value,
                                    _root_ide_package_.com.lomeone.authentication.service.AuthRegisterProvider.GITHUB.value,
                                    _root_ide_package_.com.lomeone.authentication.service.AuthRegisterProvider.KAKAO.value,
                                    _root_ide_package_.com.lomeone.authentication.service.AuthRegisterProvider.NAVER.value,
                                    _root_ide_package_.com.lomeone.authentication.service.AuthRegisterProvider.LINE.value -> TODO("Not yet implemented")
                                    else -> throw _root_ide_package_.com.lomeone.authentication.exception.OAuth2ProviderNotSupportedException(
                                        mapOf("oauth2" to registrationId)
                                    )
                                }
                            }
                    }
                    .successHandler(oAuthAuthenticationSuccessHandler)
            }

        return http.build()
    }
}

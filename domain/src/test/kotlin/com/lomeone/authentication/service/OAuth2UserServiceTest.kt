//package com.lomeone.domain.authentication.service
//
//import com.lomeone.domain.authentication.entity.Authentication
//import com.lomeone.domain.authentication.repository.AuthenticationRepository
//import com.lomeone.domain.user.service.CreateUserService
//import com.lomeone.domain.user.service.GetUserByUserTokenService
//import io.kotest.core.spec.style.BehaviorSpec
//import io.mockk.every
//import io.mockk.mockk
//import org.springframework.security.oauth2.client.registration.ClientRegistration
//import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
//import org.springframework.security.oauth2.core.user.OAuth2User
//
//class OAuth2UserServiceTest : BehaviorSpec({
//    val authenticationRepository: AuthenticationRepository = mockk()
//    val createUserService: CreateUserService = mockk()
//    val getUserByUserTokenService: GetUserByUserTokenService = mockk()
//    val registerAuthenticationService: RegisterAuthenticationService = mockk()
//    val oAuth2UserService = OAuth2UserService(
//        authenticationRepository = authenticationRepository,
//        createUserService = createUserService,
//        getUserByUserTokenService = getUserByUserTokenService,
//        registerAuthenticationService = registerAuthenticationService
//    )
//
//    Given("oauth2 인증 정보가 이미 존재하면") {
//        val authentication = mockk<Authentication>()
//        every { authentication.uid } returns "authenticate-uid"
//
//        val oAuth2UserRequest = mockk<OAuth2UserRequest>()
//        val clientRegistration = mockk<ClientRegistration>()
//        val oAuth2User = mockk<OAuth2User>()
//
//
//        every { authenticationRepository.findByUid(any()) } returns mockk()
//
//        val oauth2User = oAuth2UserService.loadUser(oAuth2UserRequest)
//    }
//
//})

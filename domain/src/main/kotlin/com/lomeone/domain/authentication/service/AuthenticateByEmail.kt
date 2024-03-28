//package com.lomeone.domain.authentication.service
//
//import org.springframework.security.authentication.AuthenticationManager
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
//import org.springframework.stereotype.Service
//
//@Service
//class AuthenticateByEmail(
//    private val jwtTokenProvider: JwtTokenProvider,
//    private val authenticationManager: AuthenticationManager
//): Authenticate<AuthenticateByEmailCommand> {
//    override fun authenticate(command: AuthenticateByEmailCommand): TokenInfo {
//        val authenticationToken = UsernamePasswordAuthenticationToken(command.email, command.password)
//        val authentication = authenticationManager.authenticate(authenticationToken)
//        return jwtTokenProvider.issueToken(authentication)
//    }
//}
//
//data class AuthenticateByEmailCommand(
//    val email: String,
//    val password: String
//)

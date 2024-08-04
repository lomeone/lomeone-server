package com.lomeone.application.graphql.api.authentication

import com.lomeone.domain.authentication.entity.AuthProvider
import com.lomeone.domain.authentication.service.AuthenticateByEmail
import com.lomeone.domain.authentication.service.AuthenticateByEmailCommand
import com.lomeone.domain.authentication.service.TokenInfo
import com.lomeone.domain.user.service.CreateUserCommand
import com.lomeone.domain.user.service.CreateUserService
import com.lomeone.generated.types.AuthenticateResult
import com.lomeone.generated.types.SignInByEmailInput
import com.lomeone.generated.types.SignUpByEmailInput
import com.lomeone.generated.types.SignUpByEmailResult
import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsMutation
import com.netflix.graphql.dgs.InputArgument

@DgsComponent
class AuthenticationDataFetCher(
    private val createUserService: CreateUserService,
    private val authenticateByEmail: AuthenticateByEmail
) {
    @DgsMutation
    fun signUpByEmail(@InputArgument input: SignUpByEmailInput): SignUpByEmailResult {
        val command = CreateUserCommand(
            userInfo = CreateUserCommand.UserInfo(
                name = input.name,
                nickname = input.nickname,
                email = input.email,
                phoneNumber = input.phoneNumber,
                birthday = input.birthday
            ),
            authenticationInfo = CreateUserCommand.AuthenticationInfo(
                email = input.email,
                password = input.password,
                provider = AuthProvider.EMAIL
            )
        )

        val result = createUserService.createUser(command)

        return SignUpByEmailResult(result.authenticationUid)
    }

    @DgsMutation
    fun signInByEmail(@InputArgument input: SignInByEmailInput): AuthenticateResult {
        val command = AuthenticateByEmailCommand(email = input.email, password = input.password)
        val tokenInfo = authenticateByEmail.authenticate(command)
        return convertTokenInfoToAuthenticateResult(tokenInfo)
    }

    private fun convertTokenInfoToAuthenticateResult(tokenInfo: TokenInfo): AuthenticateResult =
        AuthenticateResult(
            accessToken = tokenInfo.accessToken,
            refreshToken = tokenInfo.refreshToken,
            expiresIn = tokenInfo.expiresIn
        )
}

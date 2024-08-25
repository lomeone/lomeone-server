package com.lomeone.application.graphql.api.authentication

import com.lomeone.domain.authentication.entity.AuthProvider
import com.lomeone.domain.user.service.CreateUserCommand
import com.lomeone.domain.user.service.CreateUserService
import com.lomeone.generated.types.SignUpByEmailInput
import com.lomeone.generated.types.SignUpByEmailResult
import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsMutation
import com.netflix.graphql.dgs.InputArgument

@DgsComponent
class AuthenticationDataFetCher(
    private val createUserService: CreateUserService,
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
}

package com.lomeone.application.graphql.authentication

import com.lomeone.LomeoneApplication
import com.lomeone.domain.authentication.service.AuthenticateByEmail
import com.lomeone.domain.authentication.service.JwtTokenProvider
import com.lomeone.domain.user.repository.UserRepository
import com.lomeone.domain.user.service.CreateUserService
import com.lomeone.generated.types.AuthenticateResult
import com.lomeone.generated.types.SignUpByEmailResult
import com.netflix.graphql.dgs.DgsQueryExecutor
import com.netflix.graphql.dgs.autoconfig.DgsAutoConfiguration
import com.netflix.graphql.dgs.autoconfig.DgsExtendedScalarsAutoConfiguration
import io.kotest.core.spec.style.FreeSpec
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.authentication.AuthenticationManager

@SpringBootTest(classes = [
    DgsAutoConfiguration::class,
    DgsExtendedScalarsAutoConfiguration::class,
    LomeoneApplication::class,
    AuthenticationDataFetCher::class,
    CreateUserService::class,
    UserRepository::class,
    AuthenticateByEmail::class,
    AuthenticationManager::class,
    JwtTokenProvider::class
])
class AuthenticationDataFetcherTest(
    private val dgsExecutor: DgsQueryExecutor,
) : FreeSpec({
    "이메일을 이용해서 회원가입을 할 수 있다" - {
        val result: SignUpByEmailResult = dgsExecutor.executeAndExtractJsonPathAsObject("""
            mutation {
                signUpByEmail(input: {
                    email: "test@gmail.com",
                    password: "testPassword1324@",
                    name: "test-name",
                    nickname: "test-nickname",
                    phoneNumber: "+821012345678",
                    birthday: "2024-01-01"
                }) {
                    uid
                }
            }
        """.trimIndent(), "data.signUpByEmail", SignUpByEmailResult::class.java)

        println(result.uid)
    }

    "이메일을 이용해서 로그인을 할 수 있다" - {
        val result: AuthenticateResult = dgsExecutor.executeAndExtractJsonPathAsObject("""
            mutation {
                signInByEmail(input: {
                    email: "test@gmail.com",
                    password: "testPassword1324@"
                }) {
                    accessToken,
                    refreshToken,
                    expiresIn
                }
            }
        """.trimIndent(), "data.signInByEmail", AuthenticateResult::class.java)

        println(result.accessToken)
        println(result.refreshToken)
        println(result.expiresIn)
    }
})

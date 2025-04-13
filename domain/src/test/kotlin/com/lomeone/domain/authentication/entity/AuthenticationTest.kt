package com.lomeone.domain.authentication.entity

import com.lomeone.domain.authentication.exception.AuthenticationAlreadyAssociatedException
import com.lomeone.domain.authentication.exception.AuthenticationProviderIsNotEmailException
import com.lomeone.domain.authentication.exception.AuthenticationPasswordInvalidException
import com.lomeone.domain.common.entity.Email
import com.lomeone.domain.user.entity.User
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.comparables.shouldBeLessThan
import io.kotest.matchers.shouldBe
import io.mockk.mockk

class AuthenticationTest : FreeSpec({
    "계정을 생성할 때" - {
        val emailInput = Email("email@gmail.com")
        "이메일 방식이고 비밀번호가 null이 아니고 형식에 맞으면가 계정이 생성된다" - {
            val providerInput = AuthProvider.EMAIL
            val passwordInput = "testPassword1324@"

            val authentication = Authentication(
                email = emailInput,
                password = passwordInput,
                provider = providerInput,
                realm = mockk()
            )

            authentication.email shouldBe emailInput
            authentication.password shouldBe passwordInput
            authentication.provider shouldBe providerInput
        }

        "이메일 방식인데 비밀번호가 null이면 이메일 방식은 패스워드가 필요하다는 예외가 발생한다" - {
            val providerInput = AuthProvider.EMAIL
            val passwordInput = null

            shouldThrow<AuthenticationPasswordInvalidException> {
                Authentication(
                    email = emailInput,
                    password = passwordInput,
                    provider = providerInput,
                    realm = mockk()
                )
            }
        }

        "이메일 방식이 아니고 비밀번호가 null이면 계정이 생성된다" - {
            val providerInput = AuthProvider.GOOGLE
            val passwordInput = null

            val authentication = Authentication(
                email = emailInput,
                password = passwordInput,
                provider = providerInput,
                realm = mockk()
            )

            authentication.email shouldBe emailInput
            authentication.password shouldBe passwordInput
            authentication.provider shouldBe providerInput
        }

        "이메일 방식이 아니고 비밀번호가 null이 아니면 비밀번호가 null이어야한다는 예외가 발생한다" - {
            val providerInput = AuthProvider.GOOGLE
            val passwordInput = "testPassword1324@"

            shouldThrow<AuthenticationPasswordInvalidException> {
                Authentication(
                    email = emailInput,
                    password = passwordInput,
                    provider = providerInput,
                    realm = mockk()
                )
            }
        }
    }

    "유저와 연결할 때" -  {
        "유저와 연결되면 유저가 연결된다" - {
            val authentication = Authentication(
                email = Email("email@gmail.com"),
                password = "testPassword1324@",
                provider = AuthProvider.EMAIL,
                realm = mockk()
            )

            val user = mockk<User>()
            authentication.associateUser(user)

            authentication.user shouldBe user
        }

        "이미 유저와 연결되어 있으면 유저가 연결되었다는 예외가 발생한다" - {
            val authentication = Authentication(
                email = Email("email@gmail.com"),
                password = "testPassword1324@",
                provider = AuthProvider.EMAIL,
                realm = mockk()
            )

            val user = mockk<User>()
            authentication.associateUser(user)

            shouldThrow<AuthenticationAlreadyAssociatedException> {
                authentication.associateUser(user)
            }
        }
    }

    "로그인을하면 로그인 시간이 변경된다" - {
        val authentication = Authentication(
            email = Email("email@gmail.com"),
            password = "testPassword1324@",
            provider = AuthProvider.EMAIL,
            realm = mockk()
        )

        val signedInAtBefore = authentication.signedInAt
        authentication.signIn()
        val signedInAtAfter = authentication.signedInAt

        signedInAtBefore shouldBeLessThan signedInAtAfter
    }

    "비밀번호 변경은 이메일 방식의 계정만 가능하다" - {
        val emailAuthentication = Authentication(
            email = Email("email@gmail.com"),
            password = "testPassword1324@",
            provider = AuthProvider.EMAIL,
            realm = mockk()
        )

        "비밀번호가 변경된다" - {
            val passwordInput = "testPassword1324!"

            emailAuthentication.changePassword(passwordInput)
            emailAuthentication.password shouldBe passwordInput
        }

        "이메일 방식의 계정이 아니면 이메일 방식의 계정이 아니라는 예외가 발생한다" - {
            val googleAuthentication = Authentication(
                email = Email("email@gmail.com"),
                password = null,
                provider = AuthProvider.GOOGLE,
                realm = mockk()
            )

            val passwordInput = "testPassword1324!"

            shouldThrow<AuthenticationProviderIsNotEmailException> {
                googleAuthentication.changePassword(passwordInput)
            }
        }
    }
})

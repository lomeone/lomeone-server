package com.lomeone.domain.authentication.entity

import com.lomeone.domain.common.entity.Email
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.comparables.shouldBeLessThan
import io.kotest.matchers.shouldBe

class AuthenticationTest : FreeSpec({
    "계정을 생성할 때" - {
        val emailInput = Email("email@gmail.com")
        "이메일 방식이고 비밀번호가 null이 아니고 형식에 맞으면 계정이 생성된다" - {
            val providerInput = Provider.EMAIL
            val passwordInput = "testPassword1324@"

            val authentication = Authentication(
                    email = emailInput,
                    password = passwordInput,
                    provider = providerInput
            )

            authentication.email shouldBe emailInput
            authentication.password shouldBe passwordInput
            authentication.provider shouldBe providerInput
        }

        "이메일 방식이고 비밀번호가 null이 아니지만 공백이면 비밀번호가 공백이라는 예외가 발생한다" - {
            val providerInput = Provider.EMAIL
            val passwordInput = " "

            shouldThrow<Exception> {
                Authentication(
                        email = emailInput,
                        password = passwordInput,
                        provider = providerInput
                )
            }
        }

        "이메일 방식이고 비밀번호가 null이 아니지만 형식에 맞지 않으면 비밀번호가 형식에 맞지 않는다는 예외가 발생한다" - {
            val providerInput = Provider.EMAIL
            val passwordInput = "testPassword"

            shouldThrow<Exception> {
                Authentication(
                        email = emailInput,
                        password = passwordInput,
                        provider = providerInput
                )
            }
        }

        "이메일 방식인데 비밀번호가 null이면 이메일 방식은 패스워드가 필요하다는 예외가 발생한다" - {
            val providerInput = Provider.EMAIL
            val passwordInput = null

            shouldThrow<Exception> {
                Authentication(
                        email = emailInput,
                        password = passwordInput,
                        provider = providerInput
                )
            }
        }

        "이메일 방식이 아니고 비밀번호가 null이면 계정이 생성된다" - {
            val providerInput = Provider.GOOGLE
            val passwordInput = null

            val authentication = Authentication(
                    email = emailInput,
                    password = passwordInput,
                    provider = providerInput
            )

            authentication.email shouldBe emailInput
            authentication.password shouldBe passwordInput
            authentication.provider shouldBe providerInput
        }

        "이메일 방식이 아니고 비밀번호가 null이 아니면 비밀번호가 null이어야한다는 예외가 발생한다" - {
            val providerInput = Provider.GOOGLE
            val passwordInput = "testPassword1324@"

            shouldThrow<Exception> {
                Authentication(
                        email = emailInput,
                        password = passwordInput,
                        provider = providerInput
                )
            }
        }
    }

    "로그인을하면 로그인 시간이 변경된다" - {
        val authentication = Authentication(
                email = Email("email@gmail.com"),
                password = "testPassword1324@",
                provider = Provider.EMAIL
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
                provider = Provider.EMAIL
        )

        "비밀번호 형식에 맞지 않으면 비밀번호가 형식에 맞지 않는다는 예외가 발생한다" - {
            val passwordInput = "testPassword"

            shouldThrow<Exception> {
                emailAuthentication.changePassword(passwordInput)
            }
        }

        "비밀번호 형식에 맞으면 비밀번호가 변경된다" - {
            val passwordInput = "testPassword1324!"

            emailAuthentication.changePassword(passwordInput)
            emailAuthentication.password shouldBe passwordInput
        }

        "이메일 방식의 계정이 아니면 이메일 방식의 계정이 아니라는 예외가 발생한다" - {
            val googleAuthentication = Authentication(
                    email = Email("email@gmail.com"),
                    password = null,
                    provider = Provider.GOOGLE
            )

            val passwordInput = "testPassword1324!"

            shouldThrow<Exception> {
                googleAuthentication.changePassword(passwordInput)
            }
        }
    }
})

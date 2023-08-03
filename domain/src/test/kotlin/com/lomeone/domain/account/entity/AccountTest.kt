package com.lomeone.domain.account.entity

import com.lomeone.domain.common.entity.Email
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

class AccountTest : FreeSpec({
    "계정을 생성할 때" - {
        val emailInput = Email("email@gmail.com")
        "이메일 방식이고 비밀번호가 null이 아니고 형식에 맞으면 계정이 생성된다" - {
            val providerInput = Provider.EMAIL
            val passwordInput = "testPassword1324@"

            val account = Account(
                    email = emailInput,
                    password = passwordInput,
                    provider = providerInput
            )

            account.email shouldBe emailInput
            account.password shouldBe passwordInput
            account.provider shouldBe providerInput
        }

        "이메일 방식이고 비밀번호가 null이 아니지만 공백이면 비밀번호가 공백이라는 예외가 발생한다" - {
            val providerInput = Provider.EMAIL
            val passwordInput = " "

            shouldThrow<Exception> {
                Account(
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
                Account(
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
                Account(
                        email = emailInput,
                        password = passwordInput,
                        provider = providerInput
                )
            }
        }

        "이메일 방식이 아니고 비밀번호가 null이면 계정이 생성된다" - {
            val providerInput = Provider.GOOGLE
            val passwordInput = null

            val account = Account(
                    email = emailInput,
                    password = passwordInput,
                    provider = providerInput
            )

            account.email shouldBe emailInput
            account.password shouldBe passwordInput
            account.provider shouldBe providerInput
        }

        "이메일 방식이 아니고 비밀번호가 null이 아니면 비밀번호가 null이어야한다는 예외가 발생한다" - {
            val providerInput = Provider.GOOGLE
            val passwordInput = "testPassword1324@"

            shouldThrow<Exception> {
                Account(
                        email = emailInput,
                        password = passwordInput,
                        provider = providerInput
                )
            }
        }
    }
})

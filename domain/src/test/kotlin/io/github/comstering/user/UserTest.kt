package io.github.comstering.user

import io.github.comstering.user.entity.AccountType
import io.github.comstering.user.entity.User
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import java.time.ZoneId
import java.time.ZonedDateTime

class UserTest : BehaviorSpec({
    Given("이메일이 공백이면") {
        val emailInput = " "
        When("유저를 생성할 때") {
            Then("예외가 발생해서 유저를 생성할 수 없다") {
                shouldThrow<Exception> {
                    User(
                        firebaseUserToken = "user1234",
                        name = "name",
                        nickname = "nickname",
                        email = emailInput,
                        birthday = ZonedDateTime.now(),
                        accountType = AccountType.GOOGLE
                    )
                }
            }
        }
    }
    Given("이메일이 형식에 맞지 않으면") {
        val emailInput = "email"
        When("유저를 생성할 때") {
            Then("예외가 발생해서 유저를 생성할 수 없다") {
                shouldThrow<Exception> {
                    User(
                        firebaseUserToken = "user1234",
                        name = "name",
                        nickname = "nickname",
                        email = emailInput,
                        birthday = ZonedDateTime.now(),
                        accountType = AccountType.FACEBOOK
                    )
                }
            }
        }
    }
    Given("이메일이 형식에 맞으면") {
        val emailInput = "email@gmail.com"
        When("유저를 생성할 때") {
            val user = User(
                firebaseUserToken = "user1234",
                name = "name",
                nickname = "nickname",
                email = emailInput,
                birthday = ZonedDateTime.of(2000, 1, 1, 0, 0, 0, 0, ZoneId.of("Asia/Seoul")),
                accountType = AccountType.APPLE
            )
            Then("유저를 생성할 수 있다") {
                user.firebaseUserToken shouldBe "user1234"
                user.name shouldBe "name"
                user.nickname shouldBe "nickname"
                user.email.value shouldBe emailInput
                user.birthday shouldBe ZonedDateTime.of(2000, 1, 1, 0, 0, 0, 0, ZoneId.of("Asia/Seoul"))
                user.accountType shouldBe AccountType.APPLE
            }
        }
    }
})

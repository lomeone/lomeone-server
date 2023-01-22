package io.github.comstering.domain.user.entity

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import java.time.ZoneId
import java.time.ZonedDateTime

class UserTest : BehaviorSpec({
    val defaultUser = User(
        firebaseUserToken = "user1234",
        name = "name",
        nickname = "nickname",
        email = "test@gmail.com",
        birthday = ZonedDateTime.now(),
        accountType = AccountType.GOOGLE
    )

    Given("이름이 공백이 아니고, 닉네임도 공백이 아니고, 이메일도 형식에 맞으면") {
        val nameInput = "John"
        val nicknameInput = "Tomy"
        val emailInput = "email@gmail.com"
        val birthdayInput = ZonedDateTime.of(2000, 1, 1, 0, 0, 0, 0, ZoneId.of("Asia/Seoul"))
        When("유저를 생성할 때") {
            val user = User(
                firebaseUserToken = "user1234",
                name = nameInput,
                nickname = nicknameInput,
                email = emailInput,
                birthday = birthdayInput,
                accountType = AccountType.GOOGLE
            )
            Then("유저를 생성할 수 있다") {
                user.firebaseUserToken shouldBe "user1234"
                user.name shouldBe nameInput
                user.nickname shouldBe nicknameInput
                user.email.value shouldBe emailInput
                user.birthday shouldBe birthdayInput
                user.accountType shouldBe AccountType.GOOGLE
            }
        }
        When("유저 정보를 업데이트할 때") {

            defaultUser.updateUserInfo(nameInput, nicknameInput, birthdayInput)
            Then("유저 정보를 업데이트할 수 있다") {
                defaultUser.name shouldBe nameInput
                defaultUser.nickname shouldBe nicknameInput
                defaultUser.birthday shouldBe birthdayInput
            }
        }
    }
    Given("이름이 공백이면") {
        val nameInput = " "
        When("유저를 생성할 때") {
            Then("에외가 발생해서 유저를 생성할 수 없다") {
                shouldThrow<Exception> {
                    User(
                        firebaseUserToken = "user1234",
                        name = nameInput,
                        nickname = "nickname",
                        email = "email@gmail.com",
                        birthday = ZonedDateTime.now(),
                        accountType = AccountType.GOOGLE
                    )

                }
            }
        }
        When("유저 정보를 업데이트할 때") {
            Then("에외가 발생해서 유저 정보를 업데이트할 수 없다") {
                shouldThrow<Exception> {
                    defaultUser.updateUserInfo(nameInput, "nickname", ZonedDateTime.now())
                }
            }
        }
    }
    Given("닉네임이 공백이면") {
        val nicknameInput = ""
        When("유저를 생성할 때") {
            Then("예외가 발생해서 유저를 생성할 수 없다") {
                shouldThrow<Exception> {
                    User(
                        firebaseUserToken = "user1234",
                        name = "name",
                        nickname = nicknameInput,
                        email = "email@gmail.com",
                        birthday = ZonedDateTime.now(),
                        accountType = AccountType.GOOGLE
                    )
                }
            }
        }
        When("유저 정보를 업데이트할 때") {
            Then("에외가 발생해서 유저 정보를 업데이트할 수 없다") {
                shouldThrow<Exception> {
                    defaultUser.updateUserInfo("name", nicknameInput, ZonedDateTime.now())
                }
            }
        }
    }
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
})

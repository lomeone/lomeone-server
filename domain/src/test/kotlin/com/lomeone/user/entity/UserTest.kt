package com.lomeone.user.entity

import com.lomeone.domain.user.entity.AccountType
import com.lomeone.domain.user.entity.User
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
        photoUrl = "https://photo.com",
        accountType = AccountType.GOOGLE
    )

    Given("이름이 공백이 아니고, 닉네임도 공백이 아니고, 사진 경로도 공백이 아니고, 이메일도 형식에 맞으면") {
        val nameInput = "John"
        val nicknameInput = "Tomy"
        val emailInput = "email@gmail.com"
        val birthdayInput = ZonedDateTime.of(2000, 1, 1, 0, 0, 0, 0, ZoneId.of("Asia/Seoul"))
        val photoUrlInput = "https://update.photo.com"
        When("유저를 생성할 때") {
            val user = User(
                firebaseUserToken = "user1234",
                name = nameInput,
                nickname = nicknameInput,
                email = emailInput,
                birthday = birthdayInput,
                photoUrl = "https://photo.com",
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

            defaultUser.updateUserInfo(nameInput, nicknameInput, birthdayInput, photoUrlInput)
            Then("유저 정보를 업데이트할 수 있다") {
                defaultUser.name shouldBe nameInput
                defaultUser.nickname shouldBe nicknameInput
                defaultUser.birthday shouldBe birthdayInput
                defaultUser.photoUrl shouldBe photoUrlInput
            }
        }
    }
    Given("이름이 공백이면") {
        val nameInput = " "
        When("유저를 생성할 때") {
            Then("이름이 공백이라는 에외가 발생해서 유저를 생성할 수 없다") {
                shouldThrow<Exception> {
                    User(
                        firebaseUserToken = "user1234",
                        name = nameInput,
                        nickname = "nickname",
                        email = "email@gmail.com",
                        birthday = ZonedDateTime.now(),
                        photoUrl = "https://photo.com",
                        accountType = AccountType.GOOGLE
                    )

                }
            }
        }
        When("유저 정보를 업데이트할 때") {
            Then("이름이 공백이라는 에외가 발생해서 유저 정보를 업데이트할 수 없다") {
                shouldThrow<Exception> {
                    defaultUser.updateUserInfo(nameInput, "nickname", ZonedDateTime.now(), "https://photo.com")
                }
            }
        }
    }
    Given("닉네임이 공백이면") {
        val nicknameInput = ""
        When("유저를 생성할 때") {
            Then("닉네임이 공백이라는 예외가 발생해서 유저를 생성할 수 없다") {
                shouldThrow<Exception> {
                    User(
                        firebaseUserToken = "user1234",
                        name = "name",
                        nickname = nicknameInput,
                        email = "email@gmail.com",
                        birthday = ZonedDateTime.now(),
                        photoUrl = "https://photo.com",
                        accountType = AccountType.GOOGLE
                    )
                }
            }
        }
        When("유저 정보를 업데이트할 때") {
            Then("닉네임이 공백이라는 에외가 발생해서 유저 정보를 업데이트할 수 없다") {
                shouldThrow<Exception> {
                    defaultUser.updateUserInfo("name", nicknameInput, ZonedDateTime.now(), "https://photo.com")
                }
            }
        }
    }
    Given("사진 경로가 공백이면") {
        val photoUrlInput = ""
        When("유저를 생성할 때") {
            Then("사진 경로가 공백이라는 예외가 발생해서 유저를 생성할 수 없다") {
                shouldThrow<Exception> {
                    User(
                        firebaseUserToken = "user1234",
                        name = "name",
                        nickname = "nickname",
                        email = "email@gmail.com",
                        birthday = ZonedDateTime.now(),
                        photoUrl = photoUrlInput,
                        accountType = AccountType.GOOGLE
                    )
                }
            }
        }
        When("유저 정보를 업데이트할 때") {
            Then("사진 경로가 공백이라는 예외가 발생해서 유저 정보를 업데이트할 수 없다") {
                shouldThrow<Exception> {
                    defaultUser.updateUserInfo("name", "nickname", ZonedDateTime.now(), photoUrlInput)
                }
            }
        }
    }
    Given("이메일이 공백이면") {
        val emailInput = " "
        When("유저를 생성할 때") {
            Then("이메일이 공백이라는 예외가 발생해서 유저를 생성할 수 없다") {
                shouldThrow<Exception> {
                    User(
                        firebaseUserToken = "user1234",
                        name = "name",
                        nickname = "nickname",
                        email = emailInput,
                        birthday = ZonedDateTime.now(),
                        photoUrl = "https://photo.com",
                        accountType = AccountType.GOOGLE
                    )
                }
            }
        }
    }
    Given("이메일이 형식에 맞지 않으면") {
        val emailInput = "email"
        When("유저를 생성할 때") {
            Then("이메일 혁식이 맞지 않는다는 예외가 발생해서 유저를 생성할 수 없다") {
                shouldThrow<Exception> {
                    User(
                        firebaseUserToken = "user1234",
                        name = "name",
                        nickname = "nickname",
                        email = emailInput,
                        birthday = ZonedDateTime.now(),
                        photoUrl = "https://photo.com",
                        accountType = AccountType.FACEBOOK
                    )
                }
            }
        }
    }
})

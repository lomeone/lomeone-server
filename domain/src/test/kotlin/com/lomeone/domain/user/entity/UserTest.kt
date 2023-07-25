package com.lomeone.domain.user.entity

import com.lomeone.domain.common.entity.Email
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDate

const val USER_TOKEN = "user-token"

class UserTest1 : FreeSpec({
    val defaultUser = User(
        userToken = "user1234",
        name = "name",
        nickname = "nickname",
        email = Email("test@gmail.com"),
        phoneNumber = "+821012345678",
        birthday = LocalDate.now(),
        photoUrl = "https://photo.com"
    )

    "유저를 생성할 때" {
        "이름, 닉네임, 핸드폰번호, 사진 경로가 공백이 아니고 이메일이 형식에 맞아야 생성할 수 있다" - {
            val nameInput = "John"
            val nicknameInput = "Tomy"
            val emailInput = Email("email@gmail.com")
            val phoneNumberInput = "+821012345678"
            val birthdayInput = LocalDate.of(2000, 1, 1)
            val photoUrlInput = "https://update.photo.com"

            val user = User(
                userToken = USER_TOKEN,
                name = nameInput,
                nickname = nicknameInput,
                email = emailInput,
                phoneNumber = phoneNumberInput,
                birthday = birthdayInput,
                photoUrl = photoUrlInput
            )

            user.userToken shouldBe USER_TOKEN
            user.name shouldBe nameInput
            user.nickname shouldBe nicknameInput
            user.email shouldBe emailInput
            user.phoneNumber shouldBe phoneNumberInput
            user.birthday shouldBe birthdayInput
            user.photoUrl shouldBe photoUrlInput
        }

        "이름이 공백이면 이름이 공백이라는 예외가 발생한다" - {
            val nameInput = ""
            shouldThrow<Exception> {
                User(
                    userToken = USER_TOKEN,
                    name = nameInput,
                    nickname = "nickname",
                    email = Email("email@gmail.com"),
                    phoneNumber = "+821012345678",
                    birthday = LocalDate.now(),
                    photoUrl = "https://photo.com"
                )
            }
        }

        "닉네임이 공백이면 닉네임이 공백이라는 예외가 발생한다" - {
            val nicknameInput = ""
            shouldThrow<Exception> {
                User(
                    userToken = USER_TOKEN,
                    name = "name",
                    nickname = nicknameInput,
                    email = Email("email@gmail.com"),
                    phoneNumber = "+821012345678",
                    birthday = LocalDate.now(),
                    photoUrl = "https://photo.com"
                )
            }
        }

        "핸드폰 번호가 공백이면 핸드폰 번호가 공백이라는 예외가 발생한다" - {
            val phoneNumberInput = ""
            shouldThrow<Exception> {
                User(
                    userToken = USER_TOKEN,
                    name = "name",
                    nickname = "nickname",
                    email = Email("email@gmail.com"),
                    phoneNumber = phoneNumberInput,
                    birthday = LocalDate.now(),
                    photoUrl = "https://photo.com"
                )
            }
        }

        "사진 경로가 공백이면 사진 경로가 공백이라는 예외가 발생한다" - {
            val photoUrlInput = ""
            shouldThrow<Exception> {
                User(
                    userToken = USER_TOKEN,
                    name = "name",
                    nickname = "nickname",
                    email = Email("email@gmail.com"),
                    phoneNumber = "+821012345678",
                    birthday = LocalDate.now(),
                    photoUrl = photoUrlInput
                )
            }
        }
    }

    "유저 정보를 업데이트할 때" {
        "이름, 닉네임, 사진 경로가 공백이 아니면 유저 정보를 업데이트할 수 있다" - {
            val nameInput = "John"
            val nicknameInput = "Tomy"
            val birthdayInput = LocalDate.of(2000, 1, 1)
            val photoUrlInput = "https://update.photo.com"

            defaultUser.updateUserInfo(nameInput, nicknameInput, birthdayInput, photoUrlInput)

            defaultUser.name shouldBe nameInput
            defaultUser.nickname shouldBe nicknameInput
            defaultUser.birthday shouldBe birthdayInput
            defaultUser.photoUrl shouldBe photoUrlInput
        }

        "이름이 공백이면 이름이 공백이라는 예외가 발생한다" - {
            val nameInput = ""
            shouldThrow<Exception> {
                defaultUser.updateUserInfo(nameInput, "nickname", LocalDate.now(), "https://photo.com")
            }
        }

        "닉네임이 공백이면 닉네임이 공백이라는 예외가 발생한다" - {
            val nicknameInput = ""
            shouldThrow<Exception> {
                defaultUser.updateUserInfo("name", nicknameInput, LocalDate.now(), "https://photo.com")
            }
        }

        "사진 경로가 공백이면 사진 경로가 공백이라는 예외가 발생한다" - {
            val photoUrlInput = ""
            shouldThrow<Exception> {
                defaultUser.updateUserInfo("name", "nickname", LocalDate.now(), photoUrlInput)
            }
        }
    }

    "유저는 이메일을 업데이트 할 수 있다" - {
        val emailInput = Email("update@gamil.com")
        defaultUser.updateEmail(emailInput)
        defaultUser.email shouldBe emailInput
    }

    "유저는 핸드폰 번호를 업데이트할 때 " {
        "휴대폰 번호가 공백이 아니면 업데이트 할 수 있다" - {
            val phoneNumberInput = "+821012345678"
            defaultUser.updatePhoneNumber(phoneNumberInput)
            defaultUser.phoneNumber shouldBe phoneNumberInput
        }

        "휴대폰 번호가 공백이면 휴대폰 번호가 공백이라는 예외가 발생한다" - {
            val phoneNumberInput = ""
            shouldThrow<Exception> {
                defaultUser.updatePhoneNumber(phoneNumberInput)
            }
        }
    }
})

package com.lomeone.domain.user.entity

import com.lomeone.domain.authentication.entity.Authentication
import com.lomeone.domain.common.entity.Email
import com.lomeone.domain.user.exception.UserNameInvalidException
import com.lomeone.domain.user.exception.UserNicknameInvalidException
import com.lomeone.domain.user.exception.UserPhoneNumberInvalidException
import com.lomeone.domain.user.exception.UserRoleEmptyException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.shouldBe
import io.mockk.mockk
import java.time.LocalDate

class UserTest : FreeSpec({
    val defaultUser = User(
        name = "name",
        nickname = "nickname",
        email = Email("test@gmail.com"),
        phoneNumber = "+821012345678",
        birthday = LocalDate.now()
    )

    "유저를 생성할 때" - {
        "이름, 닉네임, 핸드폰번호가 공백이 아니고 이메일이 형식에 맞고 유저 역할이 있어야 생성할 수 있다" - {
            val nameInput = "John"
            val nicknameInput = "Tomy"
            val emailInput = Email("email@gmail.com")
            val phoneNumberInput = "+821012345678"
            val birthdayInput = LocalDate.of(2000, 1, 1)

            val user = User(
                name = nameInput,
                nickname = nicknameInput,
                email = emailInput,
                phoneNumber = phoneNumberInput,
                birthday = birthdayInput
            )

            user.name shouldBe nameInput
            user.nickname shouldBe nicknameInput
            user.email shouldBe emailInput
            user.phoneNumber shouldBe phoneNumberInput
            user.birthday shouldBe birthdayInput
        }

        "이름이 공백이면 이름이 공백이라는 예외가 발생해서 유저를 생성할 수 없다" - {
            val nameInput = ""
            shouldThrow<UserNameInvalidException> {
                User(
                    name = nameInput,
                    nickname = "nickname",
                    email = Email("email@gmail.com"),
                    phoneNumber = "+821012345678",
                    birthday = LocalDate.now(),
                    userRoles = mutableListOf(
                        UserRole(
                            role = Role(
                                roleName = RoleName.MEMBER
                            )
                        )
                    )
                )
            }
        }

        "닉네임이 공백이면 닉네임이 공백이라는 예외가 발생해서 유저를 생성할 수 없다" - {
            val nicknameInput = ""
            shouldThrow<UserNicknameInvalidException> {
                User(
                    name = "name",
                    nickname = nicknameInput,
                    email = Email("email@gmail.com"),
                    phoneNumber = "+821012345678",
                    birthday = LocalDate.now(),
                    userRoles = mutableListOf(
                        UserRole(
                            role = Role(
                                roleName = RoleName.MEMBER
                            )
                        )
                    )
                )
            }
        }

        "핸드폰 번호가 공백이면 핸드폰 번호가 공백이라는 예외가 발생해서 유저를 생성할 수 없다" - {
            val phoneNumberInput = ""
            shouldThrow<UserPhoneNumberInvalidException> {
                User(
                    name = "name",
                    nickname = "nickname",
                    email = Email("email@gmail.com"),
                    phoneNumber = phoneNumberInput,
                    birthday = LocalDate.now(),
                    userRoles = mutableListOf(
                        UserRole(
                            role = Role(
                                roleName = RoleName.MEMBER
                            )
                        )
                    )
                )
            }
        }

        "유저 역할이 없으면 유저 역할이 없다는 예외가 발생해서 유저를 생성할 수 없다" - {
            val userRolesInput = mutableListOf<UserRole>()
            shouldThrow<UserRoleEmptyException> {
                User(
                    name = "name",
                    nickname = "nickname",
                    email = Email("email@gmail.com"),
                    phoneNumber = "+821012345678",
                    birthday = LocalDate.now(),
                    userRoles = userRolesInput
                )
            }
        }
    }

    "유저 정보를 업데이트할 때" - {
        "이름, 닉네임이 공백이 아니면 유저 정보를 업데이트할 수 있다" - {
            val nameInput = "John"
            val nicknameInput = "Tomy"
            val birthdayInput = LocalDate.of(2000, 1, 1)

            defaultUser.updateUserInfo(nameInput, nicknameInput, birthdayInput)

            defaultUser.name shouldBe nameInput
            defaultUser.nickname shouldBe nicknameInput
            defaultUser.birthday shouldBe birthdayInput
        }

        "이름이 공백이면 이름이 공백이라는 예외가 발생해서 유저 정보를 업데이트 할 수 없다" - {
            val nameInput = ""
            shouldThrow<UserNameInvalidException> {
                defaultUser.updateUserInfo(nameInput, "nickname", LocalDate.now())
            }
        }

        "닉네임이 공백이면 닉네임이 공백이라는 예외가 발생해서 유저 정보를 업데이트 할 수 없다" - {
            val nicknameInput = ""
            shouldThrow<UserNicknameInvalidException> {
                defaultUser.updateUserInfo("name", nicknameInput, LocalDate.now())
            }
        }
    }

    "유저는 이메일을 업데이트 할 수 있다" - {
        val emailInput = Email("update@gamil.com")
        defaultUser.updateEmail(emailInput)
        defaultUser.email shouldBe emailInput
    }

    "유저는 핸드폰 번호를 업데이트할 때 " - {
        "휴대폰 번호가 공백이 아니면 업데이트 할 수 있다" - {
            val phoneNumberInput = "+821056781234"
            defaultUser.updatePhoneNumber(phoneNumberInput)
            defaultUser.phoneNumber shouldBe phoneNumberInput
        }

        "휴대폰 번호가 공백이면 휴대폰 번호가 공백이라는 예외가 발생해서 유저 정보를 업데이트 할 수 없다" - {
            val phoneNumberInput = ""
            shouldThrow<UserPhoneNumberInvalidException> {
                defaultUser.updatePhoneNumber(phoneNumberInput)
            }
        }
    }

    "유저는 인증을 추가할 수 있다" - {
        val authenticationInput: Authentication = mockk()

        defaultUser.addAuthentication(authenticationInput)
        defaultUser.authentications shouldContain authenticationInput
    }

    "유저는 역할을 추가할 수 있다" - {
        val roleInput = Role(roleName = RoleName.MEMBER)
        defaultUser.addRole(roleInput)
        defaultUser.userRoles.map { it.role } shouldContain (roleInput)
    }

    "유저는 삭제 요청할 수 있다" - {
        val deleteUser = User(
            name = "name",
            nickname = "nickname",
            email = Email("test@gmail.com"),
            phoneNumber = "+821012345678",
            birthday = LocalDate.now()
        )

        deleteUser.deleteRequest()
        deleteUser.status shouldBe UserStatus.DELETION_REQUESTED

        "삭제 요청한 유저는 복구 요청을 할 수 있다" - {
            deleteUser.restore()
            defaultUser.status shouldBe UserStatus.ACTIVE
        }
    }
})

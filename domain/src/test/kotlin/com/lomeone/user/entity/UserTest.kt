package com.lomeone.user.entity

import com.lomeone.common.entity.Email
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.shouldBe
import java.time.LocalDate

class UserTest : FreeSpec({
    val defaultUser = _root_ide_package_.com.lomeone.user.entity.User(
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

            val user = _root_ide_package_.com.lomeone.user.entity.User(
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
            shouldThrow<com.lomeone.user.exception.UserNameInvalidException> {
                _root_ide_package_.com.lomeone.user.entity.User(
                    name = nameInput,
                    nickname = "nickname",
                    email = Email("email@gmail.com"),
                    phoneNumber = "+821012345678",
                    birthday = LocalDate.now(),
                    userRoles = mutableListOf(
                        _root_ide_package_.com.lomeone.user.entity.UserRole(
                            role = _root_ide_package_.com.lomeone.user.entity.Role(
                                roleName = _root_ide_package_.com.lomeone.user.entity.RoleName.MEMBER
                            )
                        )
                    )
                )
            }
        }

        "닉네임이 공백이면 닉네임이 공백이라는 예외가 발생해서 유저를 생성할 수 없다" - {
            val nicknameInput = ""
            shouldThrow<com.lomeone.user.exception.UserNicknameInvalidException> {
                _root_ide_package_.com.lomeone.user.entity.User(
                    name = "name",
                    nickname = nicknameInput,
                    email = Email("email@gmail.com"),
                    phoneNumber = "+821012345678",
                    birthday = LocalDate.now(),
                    userRoles = mutableListOf(
                        _root_ide_package_.com.lomeone.user.entity.UserRole(
                            role = _root_ide_package_.com.lomeone.user.entity.Role(
                                roleName = _root_ide_package_.com.lomeone.user.entity.RoleName.MEMBER
                            )
                        )
                    )
                )
            }
        }

        "핸드폰 번호가 공백이면 핸드폰 번호가 공백이라는 예외가 발생해서 유저를 생성할 수 없다" - {
            val phoneNumberInput = ""
            shouldThrow<com.lomeone.user.exception.UserPhoneNumberInvalidException> {
                _root_ide_package_.com.lomeone.user.entity.User(
                    name = "name",
                    nickname = "nickname",
                    email = Email("email@gmail.com"),
                    phoneNumber = phoneNumberInput,
                    birthday = LocalDate.now(),
                    userRoles = mutableListOf(
                        _root_ide_package_.com.lomeone.user.entity.UserRole(
                            role = _root_ide_package_.com.lomeone.user.entity.Role(
                                roleName = _root_ide_package_.com.lomeone.user.entity.RoleName.MEMBER
                            )
                        )
                    )
                )
            }
        }

        "유저 역할이 없으면 유저 역할이 없다는 예외가 발생해서 유저를 생성할 수 없다" - {
            val userRolesInput = mutableListOf<com.lomeone.user.entity.UserRole>()
            shouldThrow<com.lomeone.user.exception.UserRoleEmptyException> {
                _root_ide_package_.com.lomeone.user.entity.User(
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
            shouldThrow<com.lomeone.user.exception.UserNameInvalidException> {
                defaultUser.updateUserInfo(nameInput, "nickname", LocalDate.now())
            }
        }

        "닉네임이 공백이면 닉네임이 공백이라는 예외가 발생해서 유저 정보를 업데이트 할 수 없다" - {
            val nicknameInput = ""
            shouldThrow<com.lomeone.user.exception.UserNicknameInvalidException> {
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
            shouldThrow<com.lomeone.user.exception.UserPhoneNumberInvalidException> {
                defaultUser.updatePhoneNumber(phoneNumberInput)
            }
        }
    }

    "유저는 역할을 추가할 수 있다" - {
        val roleInput =
            _root_ide_package_.com.lomeone.user.entity.Role(roleName = _root_ide_package_.com.lomeone.user.entity.RoleName.MEMBER)
        defaultUser.addRole(roleInput)
        defaultUser.userRoles.map { it.role } shouldContain (roleInput)
    }

    "유저는 삭제 요청할 수 있다" - {
        val deleteUser = _root_ide_package_.com.lomeone.user.entity.User(
            name = "name",
            nickname = "nickname",
            email = Email("test@gmail.com"),
            phoneNumber = "+821012345678",
            birthday = LocalDate.now()
        )

        deleteUser.deleteRequest()
        deleteUser.status shouldBe _root_ide_package_.com.lomeone.user.entity.UserStatus.DELETION_REQUESTED

        "삭제 요청한 유저는 복구 요청을 할 수 있다" - {
            deleteUser.restore()
            defaultUser.status shouldBe _root_ide_package_.com.lomeone.user.entity.UserStatus.ACTIVE
        }
    }
})

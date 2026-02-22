package com.lomeone.domain.authentication.entity

import com.lomeone.authentication.exception.RealmNameInvalidException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

class RealmTest : FreeSpec({
    "렐름을 생성할 때" - {
        "이름이 공백이 아니어야 생성할 수 있다" - {
            val nameInput = "test-realm"
            val realm = _root_ide_package_.com.lomeone.authentication.entity.Realm(
                name = nameInput
            )
            realm.name shouldBe nameInput
        }

        "이름이 공백이면 이름이 공백이라는 예외가 발생해서 유저를 생성할 수 없다" - {
            val nameInput = ""
            shouldThrow<com.lomeone.authentication.exception.RealmNameInvalidException> {
                _root_ide_package_.com.lomeone.authentication.entity.Realm(
                    name = nameInput
                )
            }
        }
    }
})

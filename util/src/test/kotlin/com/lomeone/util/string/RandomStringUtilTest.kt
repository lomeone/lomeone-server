package com.lomeone.util.string

import com.lomeone.util.string.RandomStringUtil.createRandomString
import io.kotest.core.spec.style.FreeSpec

class RandomStringUtilTest : FreeSpec({
    "문자목록과 생성할 길이를 입력하면 랜덤한 문자열을 만들 수 있다" - {
        createRandomString((('A'..'Z')).toSet(), 10)
    }
})

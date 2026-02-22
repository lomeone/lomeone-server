package com.lomeone.authentication.service

import com.lomeone.authentication.entity.AuthProvider
import java.time.LocalDate

interface OAuth2UserInfo {
    fun getName(): String
    fun getNickname(): String
    fun getEmail(): String
    fun getPhoneNumber(): String
    fun getBirthday(): LocalDate
    fun getProvider(): com.lomeone.authentication.entity.AuthProvider
}

data class KakaoUserInfo(
    private val attributes: MutableMap<String, Any>
) : com.lomeone.authentication.service.OAuth2UserInfo {
    override fun getName(): String = attributes.get("name") as String
    override fun getNickname(): String {
        TODO("Not yet implemented")
    }

    override fun getEmail(): String {
        TODO("Not yet implemented")
    }

    override fun getPhoneNumber(): String {
        TODO("Not yet implemented")
    }

    override fun getBirthday(): LocalDate {
        TODO("Not yet implemented")
    }

    override fun getProvider(): com.lomeone.authentication.entity.AuthProvider = _root_ide_package_.com.lomeone.authentication.entity.AuthProvider.KAKAO

}

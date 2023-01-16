package io.github.comstering.user.entity

import io.github.comstering.converter.CryptoConverter
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.ZoneId
import java.time.ZonedDateTime
import javax.persistence.Column
import javax.persistence.Convert
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Index
import javax.persistence.Table


@Entity
@Table(indexes = [Index(name = "idx_user_firebaseUserToken", columnList = "firebaseUserToken", unique = true)])
class User(
    @Column(unique = true)
    val firebaseUserToken: String,
    name: String,
    nickname: String,
    email: String,
    birthday: ZonedDateTime,
    @Enumerated(EnumType.STRING)
    val accountType: AccountType
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    var name: String
        private set

    var nickname: String
        private set

    @Convert(converter = CryptoConverter::class)
    var email: Email
        private set

    var birthday: ZonedDateTime
        private set

    @CreatedDate
    @Column(updatable = false)
    val createdAt: ZonedDateTime = ZonedDateTime.now(ZoneId.of("Asia/Seoul"))

    @LastModifiedDate
    val updatedAt: ZonedDateTime = ZonedDateTime.now(ZoneId.of("Asia/Seoul"))

    init {
        ensureNameIsNotBlank(name)
        ensureNicknameIsNotBlank(nickname)
        this.name = name
        this.nickname = nickname
        this.email = Email(email)
        this.birthday = birthday
    }

    private fun ensureNameIsNotBlank(name: String) {
        name.isBlank() && throw Exception("Name is blank")
    }

    private fun ensureNicknameIsNotBlank(nickname: String) {
        nickname.isBlank() && throw Exception("Nickname is blank")
    }

    fun updateUserInfo(name: String, nickname: String, birthday: ZonedDateTime) {
        ensureNameIsNotBlank(name)
        ensureNicknameIsNotBlank(nickname)
        this.name = name
        this.nickname = nickname
        this.birthday = birthday
    }

    @JvmInline
    value class Email(val value: String) {
        init {
            checkValidity(value)
        }

        private fun checkValidity(email: String) {
            email.isBlank() && throw Exception("Invalid email address")
            checkFormatValid(email)
        }

        private fun checkFormatValid(email: String) {
            val regex = Regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+.[a-zA-Z]\$")
            !regex.matches(email) && throw Exception("Invalid email address")
        }
    }
}

enum class AccountType {
    GOOGLE,
    FACEBOOK,
    APPLE,
    KAKAO,
    NAVER,
    EMAIL
}

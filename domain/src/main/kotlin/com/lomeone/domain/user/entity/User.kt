package com.lomeone.domain.user.entity

import com.lomeone.converter.CryptoConverter
import com.lomeone.domain.common.entity.AuditEntity
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
@Table(name = "user_entity",indexes = [Index(name = "idx_user_firebaseUserToken", columnList = "firebaseUserToken", unique = true)])
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
    @Column(unique = true)
    val firebaseUserToken: String,
    name: String,
    nickname: String,
    email: String,
    birthday: ZonedDateTime,
    photoUrl: String,
    @Enumerated(EnumType.STRING)
    val accountType: AccountType
) : AuditEntity() {
    var name: String = name
        protected set

    var nickname: String = nickname
        protected set

    @Convert(converter = CryptoConverter::class)
    val email: Email = Email(email)

    var birthday: ZonedDateTime = birthday
        protected set

    var photoUrl: String = photoUrl
        protected set

    init {
        ensureNameIsNotBlank(name)
        ensureNicknameIsNotBlank(nickname)
        ensurePhotoUrlIsNotBlank(photoUrl)
    }

    private fun ensureNameIsNotBlank(name: String) {
        name.isBlank() && throw Exception("Name is blank")
    }

    private fun ensureNicknameIsNotBlank(nickname: String) {
        nickname.isBlank() && throw Exception("Nickname is blank")
    }

    private fun ensurePhotoUrlIsNotBlank(photoUrl: String) {
        photoUrl.isBlank() && throw Exception("PhotoUrl is blank")
    }

    fun updateUserInfo(name: String, nickname: String, birthday: ZonedDateTime, photoUrl: String) {
        ensureNameIsNotBlank(name)
        ensureNicknameIsNotBlank(nickname)
        ensurePhotoUrlIsNotBlank(photoUrl)
        this.name = name
        this.nickname = nickname
        this.birthday = birthday
        this.photoUrl = photoUrl
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

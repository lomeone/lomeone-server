package com.lomeone.domain.user.entity

import com.lomeone.util.converter.AESCryptoConverter
import com.lomeone.domain.common.entity.AuditEntity
import com.lomeone.domain.common.entity.Email
import java.time.LocalDate
import javax.persistence.Column
import javax.persistence.Convert
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Index
import javax.persistence.Table


@Entity
@Table(name = "users", indexes = [Index(name = "idx_users_userToken", columnList = "userToken", unique = true)])
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "users_id")
    val id: Long = 0L,
    @Column(unique = true)
    val userToken: String,
    name: String,
    nickname: String,
    email: String,
    phoneNumber: String,
    birthday: LocalDate,
    photoUrl: String,
    var activated: Boolean = true
) : AuditEntity() {
    var name: String = name
        protected set

    var nickname: String = nickname
        protected set

    @Convert(converter = AESCryptoConverter::class)
    var email: Email = Email(email)
        protected set

    var phoneNumber: String = phoneNumber
        protected set

    var birthday: LocalDate = birthday
        protected set

    @Column(length = 4096)
    var photoUrl: String = photoUrl
        protected set

    init {
        ensureNameIsNotBlank(name)
        ensureNicknameIsNotBlank(nickname)
        ensurePhoneNumberIsNotBlank(phoneNumber)
        ensurePhotoUrlIsNotBlank(photoUrl)
    }

    private fun ensureNameIsNotBlank(name: String) {
        name.isBlank() && throw Exception("Name is blank")
    }

    private fun ensureNicknameIsNotBlank(nickname: String) {
        nickname.isBlank() && throw Exception("Nickname is blank")
    }

    private fun ensurePhoneNumberIsNotBlank(phoneNumber: String) {
        phoneNumber.isBlank() && throw Exception("PhoneNumber is blank")
    }

    private fun ensurePhotoUrlIsNotBlank(photoUrl: String) {
        photoUrl.isBlank() && throw Exception("PhotoUrl is blank")
    }

    fun updateUserInfo(name: String, nickname: String, birthday: LocalDate, photoUrl: String) {
        ensureNameIsNotBlank(name)
        ensureNicknameIsNotBlank(nickname)
        ensurePhotoUrlIsNotBlank(photoUrl)
        this.name = name
        this.nickname = nickname
        this.birthday = birthday
        this.photoUrl = photoUrl
    }

    fun updateEmail(email: String) {
        this.email = Email(email)
    }

    fun updatePhoneNumber(phoneNumber: String) {
        ensurePhoneNumberIsNotBlank(phoneNumber)
        this.phoneNumber = phoneNumber
    }

    fun inactivate() {
        this.activated = false
    }
}

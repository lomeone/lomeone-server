package com.lomeone.domain.user.entity

import com.lomeone.domain.authentication.entity.Authentication
import com.lomeone.util.converter.AESCryptoConverter
import com.lomeone.domain.common.entity.AuditEntity
import com.lomeone.domain.common.entity.Email
import java.time.LocalDate
import java.util.UUID
import jakarta.persistence.Column
import jakarta.persistence.Convert
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToMany
import jakarta.persistence.Table


@Entity
@Table(name = "users", indexes = [Index(name = "idx_users_userToken_u1", columnList = "userToken", unique = true)])
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "users_id")
    val id: Long = 0L,
    name: String,
    nickname: String,
    email: Email,
    phoneNumber: String,
    birthday: LocalDate
) : AuditEntity() {
    @Column(unique = true)
    val userToken: String = UUID.randomUUID().toString()
    var name: String = name
        protected set

    var nickname: String = nickname
        protected set

    @Convert(converter = AESCryptoConverter::class)
    var email: Email = email
        protected set

    var phoneNumber: String = phoneNumber
        protected set

    var birthday: LocalDate = birthday
        protected set

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id")
    private val _authentications: MutableList<Authentication> = mutableListOf()
    val authentications: List<Authentication> get() = _authentications

    init {
        ensureNameIsNotBlank(name)
        ensureNicknameIsNotBlank(nickname)
        ensurePhoneNumberIsNotBlank(phoneNumber)
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

    fun updateUserInfo(name: String, nickname: String, birthday: LocalDate) {
        ensureNameIsNotBlank(name)
        ensureNicknameIsNotBlank(nickname)
        this.name = name
        this.nickname = nickname
        this.birthday = birthday
    }

    fun updateEmail(email: Email) {
        this.email = email
    }

    fun updatePhoneNumber(phoneNumber: String) {
        ensurePhoneNumberIsNotBlank(phoneNumber)
        this.phoneNumber = phoneNumber
    }
}

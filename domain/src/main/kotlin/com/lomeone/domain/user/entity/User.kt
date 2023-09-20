package com.lomeone.domain.user.entity

import com.lomeone.domain.account.entity.Account
import com.lomeone.util.converter.AESCryptoConverter
import com.lomeone.domain.common.entity.AuditEntity
import com.lomeone.domain.common.entity.Email
import java.time.LocalDate
import javax.persistence.Column
import javax.persistence.Convert
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Index
import javax.persistence.JoinColumn
import javax.persistence.OneToMany
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
    email: Email,
    phoneNumber: String,
    birthday: LocalDate
) : AuditEntity() {
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
    private val _accounts: MutableList<Account> = mutableListOf()
    val accounts: List<Account> get() = _accounts

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

package com.lomeone.domain.user.entity

import com.lomeone.domain.authentication.entity.Authentication
import com.lomeone.util.converter.EmailCryptoConverter
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
@Table(name = "users", indexes = [
    Index(name = "idx_users_user_token_u1", columnList = "userToken", unique = true),
    Index(name = "idx_users_email_u2", columnList = "email", unique = true),
    Index(name = "idx_users_phone_number_u3", columnList = "phoneNumber", unique = true)
])
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "users_id")
    val id: Long = 0L,
    name: String,
    nickname: String,
    email: Email,
    phoneNumber: String,
    birthday: LocalDate,
    userRoles: MutableList<UserRole> = mutableListOf(
        UserRole(
            role = Role(
                roleName = RoleName.MEMBER
            )
        )
    )
) : AuditEntity() {
    @Column(unique = true)
    val userToken: String = UUID.randomUUID().toString()

    var name: String = name
        protected set

    var nickname: String = nickname
        protected set

    @Convert(converter = EmailCryptoConverter::class)
    var email: Email = email
        protected set

    var phoneNumber: String = phoneNumber
        protected set

    var birthday: LocalDate = birthday
        protected set

    @OneToMany
    @JoinColumn(name = "users_id")
    private val _authentications: MutableList<Authentication> = mutableListOf()
    val authentications: List<Authentication> get() = _authentications

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "users_id")
    private val _userRoles: MutableList<UserRole> = userRoles
    val userRoles: List<UserRole> get() = _userRoles

    init {
        ensureNameIsNotBlank(name)
        ensureNicknameIsNotBlank(nickname)
        ensurePhoneNumberIsNotBlank(phoneNumber)
        ensureUserRoleIsNotEmpty(userRoles)
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

    private fun ensureUserRoleIsNotEmpty(userRoles: MutableList<UserRole>) {
        if (userRoles.isEmpty()) {
            throw Exception("User role must be not empty")
        }
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

    fun addAuthentication(authentication: Authentication) {
        this._authentications.add(authentication)
    }

    fun addRole(role: Role) {
        _userRoles.add(UserRole(role = role))
    }
}

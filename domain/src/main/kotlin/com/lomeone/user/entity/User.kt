package com.lomeone.user.entity

import com.lomeone.util.converter.EmailCryptoConverter
import com.lomeone.common.entity.AuditEntity
import com.lomeone.common.entity.Email
import com.lomeone.eunoia.kotlin.util.string.StringUtils.generateRandomString
import java.time.LocalDate
import jakarta.persistence.Column
import jakarta.persistence.Convert
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
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
    @Column(name = "user_id")
    val id: Long = 0L,
    name: String,
    nickname: String,
    email: Email,
    phoneNumber: String,
    birthday: LocalDate,
    userRoles: MutableList<com.lomeone.user.entity.UserRole> = mutableListOf(
        _root_ide_package_.com.lomeone.user.entity.UserRole(
            role = _root_ide_package_.com.lomeone.user.entity.Role(
                roleName = _root_ide_package_.com.lomeone.user.entity.RoleName.MEMBER
            )
        )
    )
) : AuditEntity() {
    @Column(unique = true)
    val userToken: String = generateRandomString((('0'..'9') + ('a'..'z') + ('A'..'Z')).toSet(), 8)

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

    @Enumerated(EnumType.STRING)
    var status: com.lomeone.user.entity.UserStatus = _root_ide_package_.com.lomeone.user.entity.UserStatus.ACTIVE
        protected set

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private val _userRoles: MutableList<com.lomeone.user.entity.UserRole> = userRoles
    val userRoles: List<com.lomeone.user.entity.UserRole> get() = this._userRoles

    init {
        ensureNameIsNotBlank(name)
        ensureNicknameIsNotBlank(nickname)
        ensurePhoneNumberIsNotBlank(phoneNumber)
        ensureUserRoleIsNotEmpty(userRoles)
    }

    private fun ensureNameIsNotBlank(name: String) {
        name.isBlank() && throw _root_ide_package_.com.lomeone.user.exception.UserNameInvalidException(
            message = "Invalid name: it cannot be blank. Please enter a valid name.",
            detail = mapOf("name" to name)
        )
    }

    private fun ensureNicknameIsNotBlank(nickname: String) {
        nickname.isBlank() && throw _root_ide_package_.com.lomeone.user.exception.UserNicknameInvalidException(
            message = "Invalid nickname: it cannot be blank. Please enter a valid nickname.",
            detail = mapOf("nickname" to nickname)
        )
    }

    private fun ensurePhoneNumberIsNotBlank(phoneNumber: String) {
        phoneNumber.isBlank() && throw _root_ide_package_.com.lomeone.user.exception.UserPhoneNumberInvalidException(
            message = "Invalid phone number: it cannot be blank. Please enter a valid phone number.",
            detail = mapOf("phone_number" to phoneNumber)
        )
    }

    private fun ensureUserRoleIsNotEmpty(userRoles: List<com.lomeone.user.entity.UserRole>) {
        userRoles.isEmpty() && throw _root_ide_package_.com.lomeone.user.exception.UserRoleEmptyException(mapOf(("userRoles" to userRoles)))
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

    fun addRole(role: com.lomeone.user.entity.Role) {
        this._userRoles.add(_root_ide_package_.com.lomeone.user.entity.UserRole(role = role))
    }

    fun deleteRequest() {
        this.status = _root_ide_package_.com.lomeone.user.entity.UserStatus.DELETION_REQUESTED
    }

    fun restore() {
        this.status = _root_ide_package_.com.lomeone.user.entity.UserStatus.ACTIVE
    }
}

enum class UserStatus {
    ACTIVE,                 // 활성 유저
    INACTIVE,               // 비활성 유저(휴면 유저, 미인증 유저)
    SUSPENDED,              // 사용 불가 유저(규정 미준수 및 블랙리스트 유저)
    DELETION_REQUESTED,     // 삭제 요청 유저
    DELETED                 // 삭제 완료 유저
}

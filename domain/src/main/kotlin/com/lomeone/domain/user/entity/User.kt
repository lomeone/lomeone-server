package com.lomeone.domain.user.entity

import com.lomeone.domain.authentication.entity.Authentication
import com.lomeone.util.converter.EmailCryptoConverter
import com.lomeone.domain.common.entity.AuditEntity
import com.lomeone.domain.common.entity.Email
import com.lomeone.domain.user.exception.UserNameInvalidException
import com.lomeone.domain.user.exception.UserNicknameInvalidException
import com.lomeone.domain.user.exception.UserPhoneNumberInvalidException
import com.lomeone.domain.user.exception.UserRoleEmptyException
import com.lomeone.util.string.RandomStringUtil.createRandomString
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
    val userToken: String = createRandomString((('0'..'9') + ('a'..'z') + ('A'..'Z')).toSet(), 8)

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
    var status: UserStatus = UserStatus.ACTIVE
        protected set

    @OneToMany
    @JoinColumn(name = "users_id")
    private val _authentications: MutableList<Authentication> = mutableListOf()
    val authentications: List<Authentication> get() = this._authentications

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "users_id")
    private val _userRoles: MutableList<UserRole> = userRoles
    val userRoles: List<UserRole> get() = this._userRoles

    init {
        ensureNameIsNotBlank(name)
        ensureNicknameIsNotBlank(nickname)
        ensurePhoneNumberIsNotBlank(phoneNumber)
        ensureUserRoleIsNotEmpty(userRoles)
    }

    private fun ensureNameIsNotBlank(name: String) {
        name.isBlank() && throw UserNameInvalidException(message = "User name is blank", detail = mapOf("name" to name))
    }

    private fun ensureNicknameIsNotBlank(nickname: String) {
        nickname.isBlank() && throw UserNicknameInvalidException(message = "User nickname is blank", detail = mapOf("nickname" to nickname))
    }

    private fun ensurePhoneNumberIsNotBlank(phoneNumber: String) {
        phoneNumber.isBlank() && throw UserPhoneNumberInvalidException(message = "User phone number is blank", detail = mapOf("phone_number" to phoneNumber))
    }

    private fun ensureUserRoleIsNotEmpty(userRoles: MutableList<UserRole>) {
        userRoles.isEmpty() && throw UserRoleEmptyException()
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
        this._userRoles.add(UserRole(role = role))
    }

    fun deleteRequest() {
        this.status = UserStatus.DELETION_REQUESTED
    }

    fun restore() {
        this.status = UserStatus.ACTIVE
    }
}

enum class UserStatus {
    ACTIVE,                 // 활성 유저
    INACTIVE,               // 비활성 유저(휴면 유저, 미인증 유저)
    SUSPENDED,              // 사용 불가 유저(규정 미준수 및 블랙리스트 유저)
    DELETION_REQUESTED,     // 삭제 요청 유저
    DELETED                 // 삭제 완료 유저
}

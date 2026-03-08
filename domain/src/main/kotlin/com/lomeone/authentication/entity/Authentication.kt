package com.lomeone.authentication.entity

import com.lomeone.common.entity.AuditEntity
import com.lomeone.common.entity.Email
import com.lomeone.eunoia.kotlin.util.string.StringUtils.generateRandomString
import com.lomeone.util.converter.EmailCryptoConverter
import java.time.LocalDateTime
import jakarta.persistence.Column
import jakarta.persistence.Convert
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "authentications", indexes = [
    Index(name = "idx_authentications_uid_u1", columnList = "uid", unique = true),
    Index(name = "idx_authentications_email_provider_m1", columnList = "email, provider")
])
class Authentication(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "authentication_id")
    val id: Long = 0L,

    @Column(unique = true)
    val uid: String = generateRandomString((('0'..'9') + ('a'..'z') + ('A'..'Z')).toSet(), 8),

    @Convert(converter = EmailCryptoConverter::class)
    val email: Email,

    password: String? = null,

    @Enumerated(EnumType.STRING)
    val provider: com.lomeone.authentication.entity.AuthProvider,

    @ManyToOne
    @JoinColumn(name = "realm_id")
    val realm: com.lomeone.authentication.entity.Realm,
) : AuditEntity() {
    var password: String? = password
        protected set

    var signedInAt: LocalDateTime = LocalDateTime.now()
        protected set

    var passwordUpdatedAt: LocalDateTime = LocalDateTime.now()
        protected set

    @ManyToOne
    @JoinColumn(name = "user_id")
    var user: com.lomeone.user.entity.User? = null
        protected set

    init {
        checkPasswordByProvider(password)
    }

    private fun checkPasswordByProvider(password: String?) {
        checkPasswordIsNotNullIfEmailProvider(password)
        checkPasswordIsNullByOtherProvider(password)
    }

    private fun checkPasswordIsNotNullIfEmailProvider(password: String?) {
        this.provider == _root_ide_package_.com.lomeone.authentication.entity.AuthProvider.EMAIL && password == null
                && throw _root_ide_package_.com.lomeone.authentication.exception.AuthenticationPasswordInvalidException(
            message = "Authentication invalid password: Password must be not null if provider is email",
            detail = mapOf("provider" to this.provider)
        )
    }

    private fun checkPasswordIsNullByOtherProvider(password: String?) {
        this.provider != _root_ide_package_.com.lomeone.authentication.entity.AuthProvider.EMAIL && password != null
                && throw _root_ide_package_.com.lomeone.authentication.exception.AuthenticationPasswordInvalidException(
            message = "Password must be null if provider is not email",
            detail = mapOf("provider" to this.provider, "password" to password)
        )
    }

    fun associateUser(user: com.lomeone.user.entity.User) {
        ensureUserNotAssociated()
        this.user = user
    }

    private fun ensureUserNotAssociated() {
        this.user != null && throw _root_ide_package_.com.lomeone.authentication.exception.AuthenticationAlreadyAssociatedException(
            detail = mapOf()
        )
    }

    fun signIn() {
        this.signedInAt = LocalDateTime.now()
    }

    fun changePassword(password: String) {
        ensureEmailProvider()
        this.password = password
        this.passwordUpdatedAt = LocalDateTime.now()
    }

    private fun ensureEmailProvider() {
        this.provider != _root_ide_package_.com.lomeone.authentication.entity.AuthProvider.EMAIL
                && throw _root_ide_package_.com.lomeone.authentication.exception.AuthenticationProviderIsNotEmailException(
            mapOf("provider" to this.provider)
        )
    }
}

enum class AuthProvider(val value: String) {
    EMAIL("email"),
    GOOGLE("google"),
    FACEBOOK("facebook"),
    APPLE("apple"),
    GITHUB("github"),
    KAKAO("kakao"),
    NAVER("naver"),
    LINE("line")
}

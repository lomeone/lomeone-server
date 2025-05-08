package com.lomeone.domain.authentication.entity

import com.lomeone.domain.authentication.exception.AuthenticationAlreadyAssociatedException
import com.lomeone.domain.authentication.exception.AuthenticationProviderIsNotEmailException
import com.lomeone.domain.authentication.exception.AuthenticationPasswordInvalidException
import com.lomeone.domain.common.entity.AuditEntity
import com.lomeone.domain.common.entity.Email
import com.lomeone.domain.user.entity.User
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
    val provider: AuthProvider,

    @ManyToOne
    @JoinColumn(name = "realm_id")
    val realm: Realm,
) : AuditEntity() {
    var password: String? = password
        protected set

    var signedInAt: LocalDateTime = LocalDateTime.now()
        protected set

    var passwordUpdatedAt: LocalDateTime = LocalDateTime.now()
        protected set

    @ManyToOne
    @JoinColumn(name = "user_id")
    var user: User? = null
        protected set

    init {
        checkPasswordByProvider(password)
    }

    private fun checkPasswordByProvider(password: String?) {
        checkPasswordIsNotNullIfEmailProvider(password)
        checkPasswordIsNullByOtherProvider(password)
    }

    private fun checkPasswordIsNotNullIfEmailProvider(password: String?) {
        this.provider == AuthProvider.EMAIL && password == null
                && throw AuthenticationPasswordInvalidException(
                    message = "Authentication invalid password: Password must be not null if provider is email",
                    detail = mapOf("provider" to this.provider)
                )
    }

    private fun checkPasswordIsNullByOtherProvider(password: String?) {
        this.provider != AuthProvider.EMAIL && password != null
                && throw AuthenticationPasswordInvalidException(
                    message = "Password must be null if provider is not email",
                    detail = mapOf("provider" to this.provider, "password" to password)
                )
    }

    fun associateUser(user: User) {
        ensureUserNotAssociated()
        this.user = user
    }

    private fun ensureUserNotAssociated() {
        this.user != null && throw AuthenticationAlreadyAssociatedException(detail = mapOf())
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
        this.provider != AuthProvider.EMAIL
                && throw AuthenticationProviderIsNotEmailException(mapOf("provider" to this.provider))
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

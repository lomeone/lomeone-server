package com.lomeone.domain.authentication.entity

import com.lomeone.domain.common.entity.AuditEntity
import com.lomeone.domain.common.entity.Email
import com.lomeone.domain.user.entity.User
import com.lomeone.util.converter.EmailCryptoConverter
import java.time.LocalDateTime
import java.util.UUID
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
    @Column(name = "authentications_id")
    val id: Long = 0L,

    @Column(unique = true)
    val uid: String = UUID.randomUUID().toString(),

    @Convert(converter = EmailCryptoConverter::class)
    val email: Email,

    password: String? = null,

    @Enumerated(EnumType.STRING)
    val provider: AuthProvider,

    @ManyToOne
    @JoinColumn(name = "users_id")
    val user: User
) : AuditEntity() {
    var password: String? = password
        protected set

    var signedInAt: LocalDateTime = LocalDateTime.now()
        protected set

    var passwordUpdatedAt: LocalDateTime = LocalDateTime.now()
        protected set

    init {
        checkPasswordByProvider(password)
    }

    private fun checkPasswordByProvider(password: String?) {
        checkPasswordIsNotNullIfEmailProvider(password)
        checkPasswordIsNullByOtherProvider(password)
    }

    private fun checkPasswordIsNotNullIfEmailProvider(password: String?) {
        this.provider == AuthProvider.EMAIL && password == null && throw IllegalArgumentException("password must not be null")
    }

    private fun checkPasswordIsNullByOtherProvider(password: String?) {
        this.provider != AuthProvider.EMAIL && password != null && throw IllegalArgumentException("password must be null")
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
        this.provider != AuthProvider.EMAIL && throw IllegalArgumentException("provider must be EMAIL")
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

package com.lomeone.domain.authentication.entity

import com.lomeone.domain.common.entity.AuditEntity
import com.lomeone.domain.common.entity.Email
import com.lomeone.util.converter.AESCryptoConverter
import java.time.LocalDateTime
import java.util.UUID
import jakarta.persistence.Column
import jakarta.persistence.Convert
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Index
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

    @Convert(converter = AESCryptoConverter::class)
    val email: Email,

    password: String?,

    @Enumerated(EnumType.STRING)
    val provider: AuthProvider
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

        if (password != null) {
            ensurePasswordValidity(password)
        }
    }

    private fun checkPasswordIsNotNullIfEmailProvider(password: String?) {
        this.provider == AuthProvider.EMAIL && password == null && throw IllegalArgumentException("password must not be null")
    }

    private fun checkPasswordIsNullByOtherProvider(password: String?) {
        this.provider != AuthProvider.EMAIL && password != null && throw IllegalArgumentException("password must be null")
    }

    private fun ensurePasswordValidity(password: String) {
        ensurePasswordIsNotBlank(password)
        checkPasswordFormatValid(password)
    }

    private fun ensurePasswordIsNotBlank(password: String) {
        password.isBlank() && throw IllegalArgumentException("password must not be blank")
    }

    private fun checkPasswordFormatValid(password: String) {
        val regex = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[\$@\$!%*?&])[A-Za-z\\d\$@!%*?&]{10,}")
        !regex.matches(password) && throw IllegalArgumentException("password must be at least 8 characters, including at least one uppercase letter, one lowercase letter, one number and one special character")
    }

    fun signIn() {
        this.signedInAt = LocalDateTime.now()
    }

    fun changePassword(password: String) {
        ensureEmailProvider()
        ensurePasswordValidity(password)
        this.password = password
        this.passwordUpdatedAt = LocalDateTime.now()
    }

    private fun ensureEmailProvider() {
        this.provider != AuthProvider.EMAIL && throw IllegalArgumentException("provider must be EMAIL")
    }
}

enum class AuthProvider(val value: String) {
    EMAIL("EMAIL"),
    GOOGLE("GOOGLE"),
    FACEBOOK("FACEBOOK"),
    APPLE("APPLE"),
    GITHUB("GITHUB"),
    KAKAO("KAKAO"),
    NAVER("NAVER"),
    LINE("LINE")
}

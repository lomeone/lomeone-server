package io.github.comstering.user.entity

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Index
import javax.persistence.Table


@Entity
@Table(indexes = [Index(name = "idx_user_firebaseUserToken", columnList = "firebaseUserToken", unique = true)])
class User(
    @Column(unique = true)
    val firebaseUserToken: String,
    name: String,
    nickname: String,
    email: String,
    birthday: LocalDate,
    @Enumerated(EnumType.STRING)
    val accountType: AccountType
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    var name: String = name
        private set

    var nickname: String = nickname
        private set

    var email: Email = Email(email)
        private set

    var birthday: LocalDate = birthday
        private set

    @CreatedDate
    @Column(updatable = false)
    val createdAt: ZonedDateTime = ZonedDateTime.now(ZoneId.of("Asia/Seoul"))

    @LastModifiedDate
    val updatedAt: ZonedDateTime = ZonedDateTime.now(ZoneId.of("Asia/Seoul"))
}

@JvmInline
value class Email(val value: String) {
    init {
        checkValidity(value)
    }

    private fun checkValidity(email: String) {
        email.isBlank() && throw Exception("Invalid email address")
        checkFormatValid(email)
    }

    private fun checkFormatValid(email: String) {
        val regex = Regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+.[a-zA-Z]\$")
        !regex.matches(email) && throw Exception("Invalid email address")
    }
}

enum class AccountType {
    GOOGLE,
    FACEBOOK,
    APPLE,
    KAKAO,
    NAVER,
    EMAIL
}

package com.lomeone.domain.user.entity

import com.lomeone.domain.common.entity.AuditEntity
import com.lomeone.domain.common.entity.Email
import com.lomeone.util.converter.EmailCryptoConverter
import jakarta.persistence.Column
import jakarta.persistence.Convert
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.Table
import java.time.LocalDate

@Entity
@Table(name = "delete_users", indexes = [Index(name = "idx_delete_users_user_token_u1", columnList = "userToken", unique = true)])
class DeleteUser(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "delete_users_id")
    val id: Long = 0L,
    val name: String,
    val nickname: String,
    @Convert(converter = EmailCryptoConverter::class)
    val email: Email,
    val phoneNumber: String,
    val birthday: LocalDate
) : AuditEntity() {
}

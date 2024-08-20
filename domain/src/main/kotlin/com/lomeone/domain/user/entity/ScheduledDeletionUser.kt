package com.lomeone.domain.user.entity

import com.lomeone.domain.common.entity.AuditEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.Table

@Entity
@Table(name = "scheduled_delete_users", indexes = [
    Index(name = "idx_scheduled_delete_users_user_token_u1", columnList = "userToken")
])
class ScheduledDeletionUser(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "scheduled_delete_users_id")
    val id: Long = 0L,
    val userToken: String
) : AuditEntity() {
}

package com.lomeone.user.entity

import com.lomeone.common.entity.AuditEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.Table

@Entity
@Table(name = "deletion_request_users", indexes = [
    Index(name = "idx_deletion_request_user_token_status_created_at", columnList = "userToken, status, createdAt")
])
class DeletionRequest(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "deletion_request_user_id")
    val id: Long = 0L,
    val userToken: String,
    val reason: String
) : AuditEntity() {
    var status: com.lomeone.user.entity.DeletionStatus = _root_ide_package_.com.lomeone.user.entity.DeletionStatus.REQUEST
        protected set

    fun restore() {
        this.status = _root_ide_package_.com.lomeone.user.entity.DeletionStatus.RESTORE
    }
}

enum class DeletionStatus {
    REQUEST,    // 삭제 요청
    RESTORE,    // 복구
    DELETED     // 삭제 완료
}

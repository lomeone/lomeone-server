package com.lomeone.domain.user.entity

import com.lomeone.domain.common.entity.AuditEntity
import jakarta.persistence.*

@Entity
@Table(name = "deletion_request_users", indexes = [
    Index(name = "idx_deletion_request_users_user_token_u1", columnList = "userToken", unique = true),
    Index(name = "idx_deletion_request_users_user_token_created_at_m1", columnList = "userToken, createdAt")
])
class DeletionRequest(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "deletion_request_users_id")
    val id: Long = 0L,
    val userToken: String,
    val reason: String
) : AuditEntity() {
    var status: DeletionStatus = DeletionStatus.REQUEST
        protected set
}

enum class DeletionStatus {
    REQUEST,    // 삭제 요청
    CANCELED,   // 삭제 요청 취소
    DELETED     // 삭제 완료
}

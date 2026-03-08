package com.lomeone.user.repository

import com.lomeone.user.entity.DeletionRequest
import com.lomeone.user.entity.DeletionStatus
import org.springframework.data.jpa.repository.JpaRepository

interface DeletionRequestRepository : JpaRepository<com.lomeone.user.entity.DeletionRequest, Long> {
    fun findByUserTokenAndStatus(userToken: String, status: com.lomeone.user.entity.DeletionStatus): com.lomeone.user.entity.DeletionRequest?
}

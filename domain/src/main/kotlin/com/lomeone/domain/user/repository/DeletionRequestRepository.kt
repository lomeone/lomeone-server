package com.lomeone.domain.user.repository

import com.lomeone.domain.user.entity.DeletionRequest
import com.lomeone.domain.user.entity.DeletionStatus
import org.springframework.data.jpa.repository.JpaRepository

interface DeletionRequestRepository : JpaRepository<DeletionRequest, Long> {
    fun findByUserTokenAndStatus(userToken: String, status: DeletionStatus): DeletionRequest?
}

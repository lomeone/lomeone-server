package com.lomeone.domain.user.repository

import com.lomeone.domain.user.entity.DeletionRequest
import org.springframework.data.jpa.repository.JpaRepository

interface DeletionRequestRepository : JpaRepository<DeletionRequest, Long>

package com.lomeone.domain.authentication.repository

import com.lomeone.domain.authentication.entity.Authentication
import org.springframework.data.jpa.repository.JpaRepository

interface AuthenticationRepository : JpaRepository<Authentication, Long> {

}

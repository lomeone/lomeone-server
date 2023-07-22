package com.lomeone.domain.account.repository

import com.lomeone.domain.account.entity.Account
import org.springframework.data.jpa.repository.JpaRepository

interface AccountRepository : JpaRepository<Account, Long> {
}

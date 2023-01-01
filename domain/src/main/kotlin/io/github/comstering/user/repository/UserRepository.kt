package io.github.comstering.user.repository

import io.github.comstering.user.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long>

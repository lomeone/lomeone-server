package com.lomeone.domain.authentication.service

import com.lomeone.domain.authentication.entity.Authentication

interface Authenticate<T> {
    fun authenticate(command: T): Authentication
}

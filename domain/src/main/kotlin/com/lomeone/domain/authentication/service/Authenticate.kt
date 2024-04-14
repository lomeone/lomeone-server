package com.lomeone.domain.authentication.service

interface Authenticate<T> {
    fun authenticate(command: T): TokenInfo
}

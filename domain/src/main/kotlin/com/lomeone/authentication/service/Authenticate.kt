package com.lomeone.authentication.service

interface Authenticate<T> {
    fun authenticate(command: T): com.lomeone.authentication.service.TokenInfo
}

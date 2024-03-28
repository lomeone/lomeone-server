package com.lomeone.application.rest.authentication.request

data class JoinByEmailRequest(
    private val email: String,
    private val password: String
)

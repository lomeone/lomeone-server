package com.lomeone.application.rest.api.authentication.dto

data class RegisterEmailAuthenticationRequest(
    val email: String,
    val password: String
)

data class RegisterEmailAuthenticationResponse(
    val uid: String
)

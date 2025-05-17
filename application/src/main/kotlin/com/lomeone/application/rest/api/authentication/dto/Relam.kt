package com.lomeone.application.rest.api.authentication.dto

data class CreateRealmRequest(
    val name: String,
    val code: String? = null
)

data class CreateRealmRsponse(
    val realmCode: String
)

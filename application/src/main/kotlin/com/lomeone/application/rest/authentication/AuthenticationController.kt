package com.lomeone.application.rest.authentication

import com.lomeone.domain.authentication.service.CreateAuthenticationService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/authentication")
class AuthenticationController(
    private val createAuthenticationService: CreateAuthenticationService
) {
}

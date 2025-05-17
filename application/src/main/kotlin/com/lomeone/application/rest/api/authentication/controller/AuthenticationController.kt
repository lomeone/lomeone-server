package com.lomeone.application.rest.api.authentication.controller

import com.lomeone.application.rest.api.authentication.dto.RegisterEmailAuthenticationRequest
import com.lomeone.application.rest.api.authentication.dto.RegisterEmailAuthenticationResponse
import com.lomeone.domain.authentication.entity.AuthProvider
import com.lomeone.domain.authentication.service.RegisterAuthentication
import com.lomeone.domain.authentication.service.RegisterAuthenticationCommand
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/authentication")
class AuthenticationController(
    private val registerAuthentication: RegisterAuthentication
) {
    @PostMapping("/realms/{realm}/authentications/email")
    fun registerEmailAuthentication(
        @PathVariable realm: String,
        @RequestBody request: RegisterEmailAuthenticationRequest
    ): RegisterEmailAuthenticationResponse {
        val command = RegisterAuthenticationCommand(
            email = request.email,
            password = request.password,
            provider = AuthProvider.EMAIL,
            realmCode = realm
        )

        val result = registerAuthentication.execute(command)

        return RegisterEmailAuthenticationResponse(result.uid)
    }
}

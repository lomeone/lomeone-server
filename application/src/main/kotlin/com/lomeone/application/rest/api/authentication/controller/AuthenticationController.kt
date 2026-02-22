package com.lomeone.application.rest.api.authentication.controller

import com.lomeone.application.rest.api.authentication.dto.RegisterEmailAuthenticationRequest
import com.lomeone.application.rest.api.authentication.dto.RegisterEmailAuthenticationResponse
import com.lomeone.authentication.entity.AuthProvider
import com.lomeone.authentication.service.RegisterAuthentication
import com.lomeone.authentication.service.RegisterAuthenticationCommand
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/authentications")
class AuthenticationController(
    private val registerAuthentication: com.lomeone.authentication.service.RegisterAuthentication
) {
    @PostMapping("/realms/{realm}/email")
    fun registerEmailAuthentication(
        @PathVariable realm: String,
        @RequestBody request: RegisterEmailAuthenticationRequest
    ): RegisterEmailAuthenticationResponse {
        val command = _root_ide_package_.com.lomeone.authentication.service.RegisterAuthenticationCommand(
            email = request.email,
            password = request.password,
            provider = _root_ide_package_.com.lomeone.authentication.entity.AuthProvider.EMAIL,
            realmCode = realm
        )

        val result = registerAuthentication.execute(command)

        return RegisterEmailAuthenticationResponse(result.uid)
    }
}

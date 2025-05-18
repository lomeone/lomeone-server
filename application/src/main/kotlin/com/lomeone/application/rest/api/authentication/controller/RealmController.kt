package com.lomeone.application.rest.api.authentication.controller

import com.lomeone.application.rest.api.authentication.dto.CreateRealmRequest
import com.lomeone.application.rest.api.authentication.dto.CreateRealmRsponse
import com.lomeone.domain.authentication.service.CreateRealm
import com.lomeone.domain.authentication.service.CreateRealmCommand
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/realms")
class RealmController(
    private val createRealm: CreateRealm
) {
    @PostMapping
    fun createRealm(@RequestBody request: CreateRealmRequest): CreateRealmRsponse {
        val command = CreateRealmCommand(
            name = request.name,
            code = request.code
        )

        val result = createRealm.execute(command)

        return CreateRealmRsponse(result.realmCode)
    }
}

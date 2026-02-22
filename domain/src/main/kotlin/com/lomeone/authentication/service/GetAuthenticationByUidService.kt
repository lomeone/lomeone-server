package com.lomeone.authentication.service

import com.lomeone.authentication.entity.Authentication
import com.lomeone.authentication.repository.AuthenticationRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GetAuthenticationByUidService(
    private val authenticationRepository: com.lomeone.authentication.repository.AuthenticationRepository
) {
    @Transactional(readOnly = true)
    fun getAuthentication(query: com.lomeone.authentication.service.GetAuthenticationQuery): com.lomeone.authentication.service.GetAuthenticationResult {
        val authentication = getAuthentication(query.uid)
        return _root_ide_package_.com.lomeone.authentication.service.GetAuthenticationResult(authentication)
    }

    private fun getAuthentication(uid: String) =
        authenticationRepository.findByUid(uid) ?: throw Exception()
}

data class GetAuthenticationQuery(
    val uid: String
)

data class GetAuthenticationResult(
    val authentication: com.lomeone.authentication.entity.Authentication
)

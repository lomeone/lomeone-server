package com.lomeone.authentication.service

import com.lomeone.authentication.entity.Authentication
import com.lomeone.authentication.exception.AuthenticationNotFoundException
import com.lomeone.authentication.repository.AuthenticationRepository
import com.lomeone.authentication.entity.Realm
import com.lomeone.authentication.exception.RealmNotFoundException
import com.lomeone.authentication.repository.RealmRepository
import com.lomeone.user.entity.User
import com.lomeone.user.exception.UserNotFoundException
import com.lomeone.user.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AssociateAuthenticationToUser(
    private val authenticationRepository: com.lomeone.authentication.repository.AuthenticationRepository,
    private val realmRepository: com.lomeone.authentication.repository.RealmRepository,
    private val userRepository: com.lomeone.user.repository.UserRepository,
) {
    @Transactional
    fun execute(command: com.lomeone.authentication.service.AssociateAuthenticationToUserCommand): com.lomeone.authentication.service.AssociateAuthenticationToUserResult {
        val (userToken, uid, realmCode) = command

        val user = getUserByUserToken(userToken)

        val realm = getRealm(realmCode)

        val authentication = getAuthentication(uid, realm)

        authentication.associateUser(user)

        return _root_ide_package_.com.lomeone.authentication.service.AssociateAuthenticationToUserResult(
            userToken = user.userToken,
            uid = authentication.uid,
            realmCode = realm.code
        )
    }

    private fun getUserByUserToken(userToken: String): com.lomeone.user.entity.User =
        userRepository.findByUserToken(userToken)
            ?: throw _root_ide_package_.com.lomeone.user.exception.UserNotFoundException(mapOf("userToken" to userToken))

    private fun getRealm(realmCode: String): com.lomeone.authentication.entity.Realm =
        realmRepository.findByCode(realmCode)
            ?: throw _root_ide_package_.com.lomeone.authentication.exception.RealmNotFoundException(mapOf("realmCode" to realmCode))

    private fun getAuthentication(uid: String, realm: com.lomeone.authentication.entity.Realm): com.lomeone.authentication.entity.Authentication =
        authenticationRepository.findByUidAndRealm(uid, realm)
            ?: throw _root_ide_package_.com.lomeone.authentication.exception.AuthenticationNotFoundException(
                mapOf(
                    "uid" to uid,
                    "realm" to realm
                )
            )
}

data class AssociateAuthenticationToUserCommand(
    val userToken: String,
    val uid: String,
    val realmCode: String
)

data class AssociateAuthenticationToUserResult(
    val userToken: String,
    val uid: String,
    val realmCode: String
)

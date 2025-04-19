package com.lomeone.domain.authentication.service

import com.lomeone.domain.authentication.entity.Authentication
import com.lomeone.domain.authentication.exception.AuthenticationNotFoundException
import com.lomeone.domain.authentication.repository.AuthenticationRepository
import com.lomeone.domain.realm.entity.Realm
import com.lomeone.domain.realm.exception.RealmNotFoundException
import com.lomeone.domain.realm.repository.RealmRepository
import com.lomeone.domain.user.entity.User
import com.lomeone.domain.user.exception.UserNotFoundException
import com.lomeone.domain.user.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AssociateAuthenticationToUser(
    private val authenticationRepository: AuthenticationRepository,
    private val realmRepository: RealmRepository,
    private val userRepository: UserRepository,
) {
    @Transactional
    fun execute(command: AssociateAuthenticationToUserCommand): AssociateAuthenticationToUserResult {
        val (userToken, uid, realmCode) = command

        val user = getUserByUserToken(userToken)

        val realm = getRealm(realmCode)

        val authentication = getAuthentication(uid, realm)

        authentication.associateUser(user)

        return AssociateAuthenticationToUserResult(
            userToken = user.userToken,
            uid = authentication.uid,
            realmCode = realm.code
        )
    }

    private fun getRealm(realmCode: String): Realm =
        realmRepository.findByCode(realmCode)
            ?: throw RealmNotFoundException(mapOf("realmCode" to realmCode))

    private fun getAuthentication(uid: String, realm: Realm): Authentication =
        authenticationRepository.findByUidAndRealm(uid, realm)
            ?: throw AuthenticationNotFoundException(mapOf("uid" to uid, "realm" to realm))

    private fun getUserByUserToken(userToken: String): User =
        userRepository.findByUserToken(userToken)
            ?: throw UserNotFoundException(mapOf("userToken" to userToken))
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

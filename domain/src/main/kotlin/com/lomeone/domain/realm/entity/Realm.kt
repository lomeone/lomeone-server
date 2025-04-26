package com.lomeone.domain.realm.entity

import com.lomeone.domain.common.entity.AuditEntity
import com.lomeone.domain.realm.exception.RealmNameInvalidException
import com.lomeone.eunoia.kotlin.util.string.StringUtils.generateRandomString
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "realms")
class Realm(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "realm_id")
    val id: Long = 0L,

    name: String,

    code: String? = null,
) : AuditEntity() {
    @Column(unique = true)
    var code: String = code ?: "$name-${generateAffix()}"
        protected set

    @Column(unique = true)
    var name: String = name
        protected set

    @Enumerated(EnumType.STRING)
    var status: RealmStatus = RealmStatus.PENDING
        protected set

    private fun generateAffix() = generateRandomString((('0'..'9') + ('a'..'z') + ('A'..'Z')).toSet(), 8)

    init {
        ensureNameIsNotBlank(name)
    }

    private fun ensureNameIsNotBlank(name: String) {
        name.isBlank() && throw RealmNameInvalidException(message = "Realm name is blank", detail = mapOf("name" to name))
    }

    fun updateName(name: String) {
        this.name = name
    }

    fun updateStatus(status: RealmStatus) {
        this.status = status
    }

    fun delete() {
        this.code = "__deleted-${generateAffix()}-$code"
        this.status = RealmStatus.DELETED
    }
}

enum class RealmStatus {
    PENDING,
    ACTIVE,
    INACTIVE,
    SUSPENDED,
    DEPRECATED,
    DELETED
}

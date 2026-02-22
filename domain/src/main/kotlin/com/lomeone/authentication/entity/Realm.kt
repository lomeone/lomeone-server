package com.lomeone.authentication.entity

import com.lomeone.common.entity.AuditEntity
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
    var status: com.lomeone.authentication.entity.RealmStatus = _root_ide_package_.com.lomeone.authentication.entity.RealmStatus.PENDING
        protected set

    private fun generateAffix() = generateRandomString((('0'..'9') + ('a'..'z') + ('A'..'Z')).toSet(), 8)

    init {
        ensureNameIsNotBlank(name)
    }

    private fun ensureNameIsNotBlank(name: String) {
        name.isBlank() && throw _root_ide_package_.com.lomeone.authentication.exception.RealmNameInvalidException(
            message = "Invalid realm name: Realm name must not be blank",
            detail = mapOf("name" to name)
        )
    }

    fun updateName(name: String) {
        this.name = name
    }

    fun updateStatus(status: com.lomeone.authentication.entity.RealmStatus) {
        this.status = status
    }

    fun delete() {
        this.code = "__deleted-${generateAffix()}-$code"
        this.status = _root_ide_package_.com.lomeone.authentication.entity.RealmStatus.DELETED
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

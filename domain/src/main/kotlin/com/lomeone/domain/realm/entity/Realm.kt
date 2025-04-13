package com.lomeone.domain.realm.entity

import com.lomeone.domain.common.entity.AuditEntity
import com.lomeone.util.string.RandomStringUtil.generateRandomString
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

    code: String = "${name}-${generateRandomString((('0'..'9') + ('a'..'z') + ('A'..'Z')).toSet(), 8)}",
) : AuditEntity() {
    @Column(unique = true)
    var code: String = code
        protected set

    @Column(unique = true)
    var name: String = name
        protected set

    @Enumerated(EnumType.STRING)
    var status: RealmStatus = RealmStatus.PENDING
        protected set
}

enum class RealmStatus {
    PENDING,
    ACTIVE,
    INACTIVE,
    SUSPENDED,
    DEPRECATED,
    DELETED
}

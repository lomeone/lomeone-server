package com.lomeone.user.entity

import com.lomeone.common.entity.AuditEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.Table

@Entity
@Table(name = "roles", indexes = [Index(name = "idx_roles_role_name_u1", columnList = "roleName", unique = true)])
class Role(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    val id: Long = 0L,

    @Enumerated(EnumType.STRING)
    val roleName: RoleName
) : AuditEntity()

enum class RoleName {
    MEMBER,
    ADMIN
}

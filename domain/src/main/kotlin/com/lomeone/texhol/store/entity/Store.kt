package com.lomeone.texhol.store.entity

import com.lomeone.common.entity.AuditEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.Table

@Entity
@Table(name = "stores", indexes = [
    Index(name = "idx_stores_name_u1", columnList = "name", unique = true),
])
class Store(
    name: String,
    location: String,
    address: String?,
    imageUrl: String
) : AuditEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_id")
    val id: Long = 0L

    @Column(nullable = false)
    var name: String = name
        protected set

    @Column(nullable = false)
    var location: String = location
        protected set

    @Column(length = 500, nullable = true)
    var address: String? = address
        protected set

    @Column(nullable = false)
    var imageUrl: String = imageUrl
        protected set

    fun updateInfo(
        name: String? = null,
        location: String? = null,
        address: String? = null,
        imageUrl: String? = null
    ) {
        name?.let { this.name = it }
        location?.let { this.location = it }
        address?.let { this.address = it }
        imageUrl?.let { this.imageUrl = it }
    }
}

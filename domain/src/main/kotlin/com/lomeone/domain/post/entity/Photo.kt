package com.lomeone.domain.post.entity

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class Photo(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
    val url: String
) {
    var deleted: Boolean = false
        protected set

    init {
        ensureUrlIsNotBlank()
    }

    private fun ensureUrlIsNotBlank() {
        this.url.isBlank() && throw IllegalArgumentException("url must not be blank")
    }

    override fun equals(other: Any?) = other is Photo && other.id == this.id

    override fun hashCode() = this.id.hashCode()

    fun delete() {
        this.deleted = true
    }

    fun restore() {
        this.deleted = false
    }
}

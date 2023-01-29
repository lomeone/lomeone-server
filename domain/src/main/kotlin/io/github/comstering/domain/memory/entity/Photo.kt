package io.github.comstering.domain.memory.entity

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne

@Entity
class Photo(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
    @ManyToOne
    @JoinColumn(name = "post_id")
    val post: Post,
    val url: String,
    isMain: Boolean
) {
    var isMain: Boolean = isMain
        protected set

    var deleted: Boolean = false
        protected set

    init {
        ensureUrlIsNotBlank(this.url)
    }

    private fun ensureUrlIsNotBlank(url: String) {
        url.isBlank() && throw IllegalArgumentException("url must not be blank")
    }

    fun updateMain(isMain: Boolean) {
        this.isMain = isMain
    }

    fun delete() {
        this.deleted = true
    }

    fun restore() {
        this.deleted = false
    }
}

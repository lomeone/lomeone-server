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
    isMain: Boolean,
    isDelete: Boolean = false
) {
    var isMain: Boolean = isMain
        protected set

    var isDelete: Boolean = isDelete
        protected set

    fun updateMain(isMain: Boolean) {
        this.isMain = isMain
    }

    fun delete() {
        isDelete = true
    }

    fun restore() {
        isDelete = false
    }
}

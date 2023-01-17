package io.github.comstering.memory.entity

import io.github.comstering.user.entity.User
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.ZoneId
import java.time.ZonedDateTime
import javax.persistence.Column
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne

class Post(
    title: String,
    content: String,
    user: User
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    var title: String
        private set

    var content: String
        private set

    @CreatedDate
    @Column(updatable = false)
    val createdAt: ZonedDateTime = ZonedDateTime.now(ZoneId.of("Asia/Seoul"))

    @LastModifiedDate
    var updatedAt: ZonedDateTime = ZonedDateTime.now(ZoneId.of("Asia/Seoul"))

    @ManyToOne
    @JoinColumn(name = "user_id")
    val user: User

    init {
        ensureTitleIsNotBlank(title)
        ensureContentIsNotBlank(content)
        this.title = title
        this.content = content
        this.user = user
    }

    private fun ensureTitleIsNotBlank(title: String) {
        title.isBlank() && throw IllegalArgumentException("title must not be blank")
    }

    private fun ensureContentIsNotBlank(content: String) {
        content.isBlank() && throw IllegalArgumentException("content must not be blank")
    }
}

package io.github.comstering.memory.entity

import io.github.comstering.user.entity.User
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.ZoneId
import java.time.ZonedDateTime
import javax.persistence.AttributeOverride
import javax.persistence.AttributeOverrides
import javax.persistence.Column
import javax.persistence.Embeddable
import javax.persistence.Embedded
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne

@Entity
class Post(
    title: String,
    content: String,
    place: Place,
    user: User
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    var title: String = title
        protected set

    var content: String = content
        protected set

    @Embedded
    @AttributeOverrides(
        AttributeOverride(name = "name", column = Column(name = "place_name")),
        AttributeOverride(name = "address", column = Column(name = "place_address"))
    )
    var place: Place = place
        protected set

    @CreatedDate
    @Column(updatable = false)
    val createdAt: ZonedDateTime = ZonedDateTime.now(ZoneId.of("Asia/Seoul"))

    @LastModifiedDate
    var updatedAt: ZonedDateTime = ZonedDateTime.now(ZoneId.of("Asia/Seoul"))

    @ManyToOne
    @JoinColumn(name = "user_id")
    val user: User = user

    init {
        ensureTitleIsNotBlank(title)
        ensureContentIsNotBlank(content)
    }

    private fun ensureTitleIsNotBlank(title: String) {
        title.isBlank() && throw IllegalArgumentException("title must not be blank")
    }

    private fun ensureContentIsNotBlank(content: String) {
        content.isBlank() && throw IllegalArgumentException("content must not be blank")
    }
}

@Embeddable
class Place(
    name: String,
    address: String
) {

    var name: String = name
        protected set

    var address: String = address
        protected set

    init {
        ensurePlaceNameIsNotBlank(name)
        ensureAddressIsNotBlank(address)
    }

    private fun ensurePlaceNameIsNotBlank(placeName: String) {
        placeName.isBlank() && throw IllegalArgumentException("placeName must not be blank")
    }

    private fun ensureAddressIsNotBlank(address: String) {
        address.isBlank() && throw IllegalArgumentException("address must not be blank")
    }
}

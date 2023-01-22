package io.github.comstering.memory.entity

import io.github.comstering.common.entity.AuditEntity
import io.github.comstering.user.entity.User
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
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
    title: String,
    content: String,
    place: Place,
    @ManyToOne
    @JoinColumn(name = "user_id")
    val user: User
) : AuditEntity() {
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

    override fun equals(other: Any?) = other is Post && other.id == this.id

    override fun hashCode() = this.id.hashCode()

    fun updatePost(title: String, content: String, place: Place) {
        ensureTitleIsNotBlank(title)
        ensureContentIsNotBlank(content)
        this.title = title
        this.content = content
        this.place = place
    }
}

@Embeddable
class Place(
    @Column
    val name: String,
    @Column
    val address: String
) {
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

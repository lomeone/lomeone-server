package com.lomeone.texhol.game.entity

import com.lomeone.common.entity.AuditEntity
import com.lomeone.texhol.store.entity.Store
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(
    name = "game_types",
    indexes = [
        Index(
            name = "idx_game_types_store_id_name_u1",
            columnList = "store_id, name",
            unique = true
        )
    ]
)
class GameType(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    val store: Store,

    val name: String,

    @Enumerated(EnumType.STRING)
    val scheduleType: ScheduleType,

    val dayOfWeek: Int?, // 0-6 (Sunday-Saturday), only used when scheduleType is WEEKLY

    val description: String?
) : AuditEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "game_type_id")
    val id: Long = 0L

    init {
        if (scheduleType == ScheduleType.WEEKLY) {
            require(dayOfWeek != null && dayOfWeek in 0..6) {
                "dayOfWeek must be between 0 and 6 for WEEKLY schedule type"
            }
        }
    }

    fun belongsTo(store: Store): Boolean {
        return if (this.store.id != 0L && store.id != 0L) {
            this.store.id == store.id
        } else {
            this.store === store
        }
    }
}

enum class ScheduleType {
    DAILY,    // 매일 갱신
    WEEKLY,   // 매주 특정 요일 갱신
    MONTHLY,  // 매월 갱신
    CUSTOM    // 수동 관리
}

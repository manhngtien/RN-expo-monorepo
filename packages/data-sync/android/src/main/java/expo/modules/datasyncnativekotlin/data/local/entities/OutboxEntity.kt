package expo.modules.datasyncnativekotlin.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.android.identity.util.UUID

@Entity(tableName = "outbox_events")
data class OutboxEntity(
    @PrimaryKey val eventId: String = UUID.randomUUID().toString(),

    // ID của Pokemon bị tác động
    val aggregateId: String,

    // Ví dụ: "INSERT_POKEMON", "UPDATE_TYPE"
    val eventType: String,

    // JSON String chứa dữ liệu thay đổi
    val payload: String,

    // Trạng thái: PENDING, SYNCING, COMPLETED, FAILED
    val status: String = "PENDING",

    // Số lần đã thử đồng bộ
    val retryCount: Int = 0,

    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
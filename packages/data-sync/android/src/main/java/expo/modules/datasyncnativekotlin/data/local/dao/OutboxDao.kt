package expo.modules.datasyncnativekotlin.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import expo.modules.datasyncnativekotlin.data.local.entities.OutboxEntity

@Dao
interface OutboxDao : BaseDao<OutboxEntity> {

    // 1. Worker sẽ gọi hàm này để "vét" tất cả các tác vụ chưa được đồng bộ
    // Sắp xếp theo thứ tự thời gian để đảm bảo tác vụ cũ được đồng bộ trước (FIFO)
    @Query("SELECT * FROM outbox_events WHERE status = 'PENDING' ORDER BY eventId ASC")
    suspend fun getPendingEvents(): List<OutboxEntity>

    // 2. Cập nhật trạng thái nhanh (thành SYNCING, COMPLETED, hoặc FAILED)
    // mà không cần phải truyền lại nguyên cái Object OutboxEntity
    @Query("UPDATE outbox_events SET status = :newStatus WHERE eventId = :eventId")
    suspend fun updateStatus(eventId: String, newStatus: String)

    // 3. Tăng số lần thử lại (nếu gọi API lỗi hoặc truyền P2P lỗi)
    @Query("UPDATE outbox_events SET retryCount = retryCount + 1 WHERE eventId = :eventId")
    suspend fun incrementRetryCount(eventId: String)

    // 4. Dọn dẹp ổ cứng: Xóa các event đã đồng bộ thành công để tránh làm phình Database
    @Query("DELETE FROM outbox_events WHERE status = 'COMPLETED'")
    suspend fun deleteCompletedEvents()
}

package expo.modules.datasyncnativekotlin.data.mapper

import kotlinx.serialization.json.Json

object AppJson {
    val instance = Json {
        // 1. Bỏ qua các key lạ (quan trọng khi Tablet B gửi data version mới có thêm cột)
        ignoreUnknownKeys = true

        // 2. Luôn đẩy cả những trường có giá trị mặc định ra chuỗi JSON
        encodeDefaults = true

        // 3. Ép kiểu an toàn (Cứu crash cho Null và Unknown Enum)
        coerceInputValues = true

        // (Tùy chọn) Cho phép JSON format thoải mái hơn (ví dụ: key không có dấu ngoặc kép)
        isLenient = true
    }
}
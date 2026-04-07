package expo.modules.datasyncnativekotlin.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import expo.modules.datasyncnativekotlin.data.local.entities.PokemonEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PokemonDao : BaseDao<PokemonEntity> {

    // Trả về Flow để React Native có thể subscribe và cập nhật UI realtime (Reactive Programming)
    @Query("SELECT * FROM pokemon_table ORDER BY id DESC")
    fun getAllPokemons(): Flow<List<PokemonEntity>>

    @Query("SELECT * FROM pokemon_table LIMIT :limit OFFSET :offset")
    suspend fun getPokemons(limit: Int, offset: Int): List<PokemonEntity>

    @Query("SELECT COUNT(*) FROM pokemon_table")
    suspend fun getTotalCount(): Int

    // Hàm tiện ích để xóa toàn bộ dữ liệu (thường dùng khi user đăng xuất)
    @Query("DELETE FROM pokemon_table")
    suspend fun deleteAll()
}


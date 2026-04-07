package expo.modules.datasyncnativekotlin.domain.repository

import expo.modules.datasyncnativekotlin.data.local.entities.PokemonEntity
import expo.modules.datasyncnativekotlin.domain.model.PokemonPage

interface PokemonRepository {
    suspend fun getPokemonList(limit: Int, offset: Int): Result<PokemonPage>
    suspend fun savePokemonWithEvent(pokemon: PokemonEntity, isFromSync: Boolean = false)
}
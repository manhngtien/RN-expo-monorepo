package expo.modules.datasyncnativekotlin.data.repository

import expo.modules.datasyncnativekotlin.data.local.dao.OutboxDao
import expo.modules.datasyncnativekotlin.data.local.dao.PokemonDao
import expo.modules.datasyncnativekotlin.data.local.entities.PokemonEntity
import expo.modules.datasyncnativekotlin.data.remote.api.PokeApiService
import expo.modules.datasyncnativekotlin.data.remote.dto.PokemonDto
import expo.modules.datasyncnativekotlin.data.remote.dto.PokemonListResponseDto
import expo.modules.datasyncnativekotlin.data.transaction.TransactionRunner
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class PokemonRepositoryImplTest {

    private lateinit var pokemonDao: PokemonDao
    private lateinit var outboxDao: OutboxDao
    private lateinit var transactionRunner: TransactionRunner
    private lateinit var apiService: PokeApiService
    private lateinit var repository: PokemonRepositoryImpl

    @Before
    fun setup() {
        pokemonDao = mockk(relaxed = true)
        outboxDao = mockk(relaxed = true)
        apiService = mockk()
        transactionRunner = mockk()

        // TransactionRunner mock: thực thi block trực tiếp, không cần Room
        coEvery { transactionRunner.run(any<suspend () -> Any>()) } coAnswers {
            firstArg<suspend () -> Any>().invoke()
        }

        repository = PokemonRepositoryImpl(pokemonDao, outboxDao, transactionRunner, apiService)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    // ==========================================
    // getPokemonList
    // ==========================================

    @Test
    fun `getPokemonList - when API succeeds, should upsert to DB and return local data`() = runTest {
        val dtos = listOf(PokemonDto("bulbasaur", "url/1"), PokemonDto("ivysaur", "url/2"))
        val mockResponse = mockk<Response<PokemonListResponseDto>> {
            every { isSuccessful } returns true
            every { body() } returns PokemonListResponseDto(results = dtos)
        }
        coEvery { apiService.fetchPokemons(any(), any()) } returns mockResponse
        coEvery { pokemonDao.getPokemons(any(), any()) } returns listOf(
            PokemonEntity(name = "bulbasaur", url = "url/1"),
            PokemonEntity(name = "ivysaur", url = "url/2")
        )
        coEvery { pokemonDao.getTotalCount() } returns 2

        val result = repository.getPokemonList(20, 0)

        assertTrue(result.isSuccess)
        assertEquals(2, result.getOrNull()?.results?.size)
        coVerify { pokemonDao.upsertAll(any()) }
    }

    @Test
    fun `getPokemonList - when API fails, should still return local data (offline fallback)`() = runTest {
        coEvery { apiService.fetchPokemons(any(), any()) } throws RuntimeException("Network error")
        coEvery { pokemonDao.getPokemons(any(), any()) } returns listOf(
            PokemonEntity(name = "bulbasaur", url = "url/1")
        )
        coEvery { pokemonDao.getTotalCount() } returns 1

        val result = repository.getPokemonList(20, 0)

        assertTrue(result.isSuccess)
        assertEquals(1, result.getOrNull()?.results?.size)
        coVerify(exactly = 0) { pokemonDao.upsertAll(any()) }
    }

    @Test
    fun `getPokemonList - when API fails and DB is empty, should return failure`() = runTest {
        coEvery { apiService.fetchPokemons(any(), any()) } throws RuntimeException("Network error")
        coEvery { pokemonDao.getPokemons(any(), any()) } returns emptyList()
        coEvery { pokemonDao.getTotalCount() } returns 0

        val result = repository.getPokemonList(20, 0)

        assertTrue(result.isFailure)
    }

    // ==========================================
    // savePokemonWithEvent
    // ==========================================

    @Test
    fun `savePokemonWithEvent - when isFromSync is false, should save pokemon and create outbox event`() = runTest {
        val pokemon = PokemonEntity(id = 1, name = "bulbasaur", url = "url/1")

        repository.savePokemonWithEvent(pokemon, isFromSync = false)

        coVerify { pokemonDao.upsert(pokemon) }
        coVerify {
            outboxDao.upsert(match { it.eventType == "UPSERT_POKEMON" && it.aggregateId == "1" })
        }
    }

    @Test
    fun `savePokemonWithEvent - when isFromSync is true, should save pokemon without outbox event`() = runTest {
        val pokemon = PokemonEntity(id = 1, name = "bulbasaur", url = "url/1")

        repository.savePokemonWithEvent(pokemon, isFromSync = true)

        coVerify { pokemonDao.upsert(pokemon) }
        coVerify(exactly = 0) { outboxDao.upsert(any()) }
    }

    @Test
    fun `savePokemonWithEvent - should run inside a transaction`() = runTest {
        val pokemon = PokemonEntity(id = 1, name = "bulbasaur", url = "url/1")

        repository.savePokemonWithEvent(pokemon, isFromSync = false)

        coVerify { transactionRunner.run(any()) }
    }
}

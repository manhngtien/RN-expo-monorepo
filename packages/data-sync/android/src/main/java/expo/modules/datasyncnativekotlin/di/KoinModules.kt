package expo.modules.datasyncnativekotlin.di

import expo.modules.datasyncnativekotlin.core.network.AndroidNetworkMonitor
import expo.modules.datasyncnativekotlin.core.network.NetworkMonitor
import expo.modules.datasyncnativekotlin.data.manager.AndroidNfcManagerImpl
import expo.modules.datasyncnativekotlin.data.manager.FeatureFlagManagerImpl
import expo.modules.datasyncnativekotlin.data.remote.api.PokeApiService
import expo.modules.datasyncnativekotlin.data.repository.PokemonRepositoryImpl
import expo.modules.datasyncnativekotlin.data.transaction.RoomTransactionRunner
import expo.modules.datasyncnativekotlin.data.transaction.TransactionRunner
import expo.modules.datasyncnativekotlin.di.provider.provideOkHttpClient
import expo.modules.datasyncnativekotlin.di.provider.provideOutboxDao
import expo.modules.datasyncnativekotlin.di.provider.providePokemonDao
import expo.modules.datasyncnativekotlin.di.provider.provideRetrofit
import expo.modules.datasyncnativekotlin.di.provider.provideRoomDatabase
import expo.modules.datasyncnativekotlin.domain.manager.AndroidNfcManager
import expo.modules.datasyncnativekotlin.domain.manager.FeatureFlagManager
import expo.modules.datasyncnativekotlin.domain.repository.PokemonRepository
import expo.modules.datasyncnativekotlin.domain.usecase.GetPokemonListUseCase
import expo.modules.datasyncnativekotlin.presentation.facades.PokemonFacade
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit

val coreModule = module {
    // 1. FeatureFlag Module
    single<FeatureFlagManager> { FeatureFlagManagerImpl(androidContext()) }

    // 2. Database Module
    single { provideRoomDatabase(androidContext()) }
    single { providePokemonDao(get()) }
    single { provideOutboxDao(get()) }
    single<TransactionRunner> { RoomTransactionRunner(get()) }

    // 3. Network Module
    single<NetworkMonitor> { AndroidNetworkMonitor(androidContext()) }
    single { provideOkHttpClient() }
    single { provideRetrofit(get()) }

    // 4. NFC Module
    single<AndroidNfcManager> { AndroidNfcManagerImpl(androidContext()) }
}

val dataModule = module {
    single<PokeApiService> {
        get<Retrofit>().create(PokeApiService::class.java)
    }
    single<PokemonRepository> { PokemonRepositoryImpl(get(), get(), get(), get()) }
    factory { GetPokemonListUseCase(get()) }
    single { PokemonFacade(get()) }
}

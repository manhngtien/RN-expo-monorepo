package expo.modules.datasyncnativekotlin.di.provider

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import expo.modules.datasyncnativekotlin.core.network.NetworkClient
import expo.modules.datasyncnativekotlin.data.mapper.AppJson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.OkHttpClient.Builder
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

fun provideOkHttpClient(): OkHttpClient {
    return Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        // .addInterceptor(AuthInterceptor())
        .build()
}

fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder()
        .baseUrl(NetworkClient.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(AppJson.instance.asConverterFactory("application/json".toMediaType()))
        .build()
}
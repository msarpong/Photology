package org.msarpong.splash.di

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import org.msarpong.splash.service.AuthServiceApi
import org.msarpong.splash.service.SplashServiceApi
import org.msarpong.splash.util.BASE_URL
import org.msarpong.splash.util.TOKEN_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val retrofitModule = module {
    single { client }
    single { get<Retrofit>().create(SplashServiceApi::class.java) }
    single { get<Retrofit>().create(AuthServiceApi::class.java) }
}

val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
    this.level = HttpLoggingInterceptor.Level.BODY
}

val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

val retrofit: Retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BASE_URL)
    .client(client)
    .build()

val authRetrofit: Retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(TOKEN_URL)
    .client(client)
    .build()

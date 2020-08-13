package org.msarpong.photology.di

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import org.msarpong.photology.service.AuthServiceApi
import org.msarpong.photology.service.SplashServiceApi
import org.msarpong.photology.util.BASE_URL
import org.msarpong.photology.util.TOKEN_URL
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

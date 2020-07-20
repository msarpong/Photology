package org.msarpong.splash.service

import android.util.Log
import org.msarpong.splash.di.retrofit
import org.msarpong.splash.service.mapping.Unsplash
import org.msarpong.splash.util.ACCESS_KEY
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

sealed class ServiceResult {
    data class Error(val error: Throwable) : ServiceResult()
    data class Success(val pictureList: Unsplash) : ServiceResult()
}

interface ServiceReceiver {
    fun receive(result: ServiceResult)
}

class Service {
    private val service: SplashServiceApi = retrofit.create(
        SplashServiceApi::class.java
    )

    fun getHome(receiver: ServiceReceiver) {
        Log.d("onFailure_getImage", "Error")

        val photos = service.getPhoto()
        photos.enqueue(object : Callback<Unsplash> {
            override fun onFailure(call: Call<Unsplash>, t: Throwable) {
                Log.d("onFailure_getImage", "showError: $t")
            }

            override fun onResponse(call: Call<Unsplash>, response: Response<Unsplash>) {
                val success = response.body()
                val error = response.errorBody()
                if (success != null) {
                    val pictureResult = response.body()!!
                    Log.d("onResponse_getImage", "showSuccess_{}: $success")
                } else {
                    Log.d("onResponse_getImage", "showError_{}: $error")
                }
            }

        })
    }
}

interface SplashServiceApi {
    @GET("https://api.unsplash.com/photos/?client_id=cf9190b65fe2f5ad324ce4507dacbf926e7d819e996e502e6e51b72b88f1472a")
    fun getPhoto(): Call<Unsplash>
}

//interface SplashServiceApi {
//    @GET("/photos/?client_id={api_key}")
//    fun getPhoto(
//        @Path("api_key") api_key: String
//    ): Call<Unsplash>
//}
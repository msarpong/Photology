package org.msarpong.splash.service

import android.util.Log
import org.msarpong.splash.di.retrofit
import org.msarpong.splash.service.mapping.Unsplash
import org.msarpong.splash.service.mapping.UnsplashItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.GET

sealed class ServiceResult {
    data class Error(val error: Throwable) : ServiceResult()
    data class Success(val pictureList: Unsplash) : ServiceResult()
    data class Detail(val pictureList: UnsplashItem) : ServiceResult()

}

interface ServiceReceiver {
    fun receive(result: ServiceResult)
}

class Service {
    private val service: SplashServiceApi = retrofit.create(
        SplashServiceApi::class.java
    )

    fun getHome(receiver: ServiceReceiver) {

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
                    receiver.receive(ServiceResult.Success(pictureResult))
                    Log.d("onResponse_getImage", "showSuccess_{}: $success")
                } else {
                    Log.d("onResponse_getImage", "showError_{}: $error")
                }
            }

        })
    }

    fun getDetail(detailId: String, receiver: ServiceReceiver) {
        val detail = service.getDetailPhoto()
        detail.enqueue(object : Callback<UnsplashItem> {
            override fun onFailure(call: Call<UnsplashItem>, t: Throwable) {
                Log.d("onFailure_getImage", "showError: $t")
            }

            override fun onResponse(call: Call<UnsplashItem>, response: Response<UnsplashItem>) {
                val success = response.body()
                val error = response.errorBody()
                if (success != null) {
                    val pictureResult = response.body()!!
                    receiver.receive((ServiceResult.Detail(pictureResult)))
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

    @GET("https://api.unsplash.com/photos/KLbUohEjb04/?client_id=cf9190b65fe2f5ad324ce4507dacbf926e7d819e996e502e6e51b72b88f1472a")
    fun getDetailPhoto(): Call<UnsplashItem>
}



package org.msarpong.splash.service

import android.util.Log
import org.msarpong.splash.di.retrofit
import org.msarpong.splash.service.mapping.Unsplash
import org.msarpong.splash.service.mapping.UnsplashItem
import org.msarpong.splash.util.ACCESS_KEY
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

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
                    Log.d("onResponse_getImage", "showSuccess: $success")
                } else {
                    Log.d("onResponse_getImage", "showError: $error")
                }
            }

        })
    }

    fun getDetail(detailId: String, receiver: ServiceReceiver) {
        val detail = service.getDetailPhoto(detailId)
        detail.enqueue(object : Callback<UnsplashItem> {
            override fun onFailure(call: Call<UnsplashItem>, t: Throwable) {
                Log.d("onFailure_getDetail", "showError: $t")
            }

            override fun onResponse(call: Call<UnsplashItem>, response: Response<UnsplashItem>) {
                val success = response.body()
                val error = response.errorBody()
                if (success != null) {
                    val pictureResult = response.body()!!
                    receiver.receive((ServiceResult.Detail(pictureResult)))
                    Log.d("onResponse_getDetail", "showSuccess: $success")
                } else {
                    Log.d("onResponse_getDetail", "showError: $error")
                }
            }
        })
    }
}

interface SplashServiceApi {
    @GET("photos/")
    fun getPhoto(@Query("client_id") client_id: String = ACCESS_KEY): Call<Unsplash>

    @GET("/photos/{detailId}/")
    fun getDetailPhoto(
        @Path("detailId") detailId: String,
        @Query("client_id") client_id: String = ACCESS_KEY
    ): Call<UnsplashItem>
}



package org.msarpong.splash.service

import android.util.Log
import org.msarpong.splash.di.retrofit
import org.msarpong.splash.service.mapping.collection.Collection
import org.msarpong.splash.service.mapping.detail_photo.DetailPhotoResponse
import org.msarpong.splash.service.mapping.photos.PhotoResponse
import org.msarpong.splash.service.mapping.profile.Profile
import org.msarpong.splash.service.mapping.search.SearchResponse
import org.msarpong.splash.util.CLIENT_ID
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

sealed class ServiceResult {
    data class Error(val error: Throwable) : ServiceResult()
    data class Success(val pictureList: PhotoResponse) : ServiceResult()
    data class SuccessCollection(val collectionList: Collection) : ServiceResult()
    data class SuccessProfile(val profile: Profile) : ServiceResult()
    data class SuccessSearch(val search: SearchResponse) : ServiceResult()
    data class Detail(val pictureList: DetailPhotoResponse) : ServiceResult()
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
        photos.enqueue(object : Callback<PhotoResponse> {
            override fun onFailure(call: Call<PhotoResponse>, t: Throwable) {
                Log.d("onFailure_getImage", "showError: $t")
            }

            override fun onResponse(call: Call<PhotoResponse>, response: Response<PhotoResponse>) {
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

    fun getCollection(receiver: ServiceReceiver) {
        val collection = service.getPhotoCollections()
        collection.enqueue(object : Callback<Collection> {
            override fun onFailure(call: Call<Collection>, t: Throwable) {
                Log.d("onFailure_getCollection", "showError: $t")
            }

            override fun onResponse(call: Call<Collection>, response: Response<Collection>) {
                val success = response.body()
                val error = response.errorBody()

                if (success != null) {
                    val pictureResult = response.body()!!
                    receiver.receive(ServiceResult.SuccessCollection(pictureResult))
                    Log.d("onResponse_getCol", "showSuccess: $success")
                } else {
                    Log.d("onResponse_getCol", "showError: $error")
                }
            }
        })
    }

    fun getDetail(detailId: String, receiver: ServiceReceiver) {
        val detail = service.getDetailPhoto(detailId)
        detail.enqueue(object : Callback<DetailPhotoResponse> {
            override fun onFailure(call: Call<DetailPhotoResponse>, t: Throwable) {
                Log.d("onFailure_getDetail", "showError: $t")
            }

            override fun onResponse(
                call: Call<DetailPhotoResponse>,
                response: Response<DetailPhotoResponse>
            ) {
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

    fun getProfile(username: String, receiver: ServiceReceiver) {
        val profile = service.getDetailUser(username)
        profile.enqueue(object : Callback<Profile> {
            override fun onFailure(call: Call<Profile>, t: Throwable) {
                Log.d("onFailure_getProfile", "showError: $t")
            }

            override fun onResponse(call: Call<Profile>, response: Response<Profile>) {
                val success = response.body()
                val error = response.errorBody()

                if (success != null) {
                    val pictureResult = response.body()!!
                    receiver.receive(ServiceResult.SuccessProfile(pictureResult))
                    Log.d("onResponse_getProfile", "showSuccess: $success")
                } else {
                    Log.d("onResponse_getProfile", "showError: $error")
                }
            }
        })
    }

    fun getPhotoUser(username: String, receiver: ServiceReceiver) {

        val photos = service.getDetailPhotoUser(username)
        photos.enqueue(object : Callback<PhotoResponse> {
            override fun onFailure(call: Call<PhotoResponse>, t: Throwable) {
                Log.d("onFailure_getProfilePh", "showError: $t")
            }

            override fun onResponse(call: Call<PhotoResponse>, response: Response<PhotoResponse>) {
                val success = response.body()
                val error = response.errorBody()
                if (success != null) {
                    val pictureResult = response.body()!!
                    receiver.receive(ServiceResult.Success(pictureResult))
                    Log.d("onResponse_getProfilePh", "showSuccess: $success")
                } else {
                    Log.d("onResponse_getProfilePh", "showError: $error")
                }
            }

        })
    }

    fun getSearchQuery(search_type: String, query: String, receiver: ServiceReceiver) {

        val queryResult = service.getSearch(search_type, query)
        queryResult.enqueue((object : Callback<SearchResponse> {
            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                Log.d("onFailure_getSearch", "showError: $t")
            }

            override fun onResponse(
                call: Call<SearchResponse>,
                response: Response<SearchResponse>
            ) {
                val success = response.body()
                val error = response.errorBody()
                if (success != null) {
                    val pictureResult = response.body()!!
                    receiver.receive(ServiceResult.SuccessSearch(pictureResult))
                    Log.d("onResponse_getSearch", "showSuccess: $success")
                } else {
                    Log.d("onResponse_getSearch", "showError: $error")
                }
            }

        }))
    }
}

interface SplashServiceApi {
    @GET("photos/")
    fun getPhoto(@Query("client_id") client_id: String = CLIENT_ID): Call<PhotoResponse>

    @GET("/photos/{detailId}/")
    fun getDetailPhoto(
        @Path("detailId") detailId: String,
        @Query("client_id") client_id: String = CLIENT_ID
    ): Call<DetailPhotoResponse>

    @GET("collections/")
    fun getPhotoCollections(@Query("client_id") client_id: String = CLIENT_ID): Call<Collection>

    @GET("/users/{username}/")
    fun getDetailUser(
        @Path("username") detailId: String,
        @Query("client_id") client_id: String = CLIENT_ID
    ): Call<Profile>

    @GET("/users/{username}/photos")
    fun getDetailPhotoUser(
        @Path("username") detailId: String,
        @Query("client_id") client_id: String = CLIENT_ID
    ): Call<PhotoResponse>

    @GET("/search/{search_type}")
    fun getSearch(
        @Path("search_type") search_type: String,
        @Query("query") query: String,
        @Query("client_id") client_id: String = CLIENT_ID
    ): Call<SearchResponse>
}



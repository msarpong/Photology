package org.msarpong.splash.service

import android.util.Log
import org.msarpong.splash.di.retrofit
import org.msarpong.splash.service.mapping.auth.user.UserResponse
import org.msarpong.splash.service.mapping.collection.Collection
import org.msarpong.splash.service.mapping.detail_photo.DetailPhotoResponse
import org.msarpong.splash.service.mapping.following.FollowingResponse
import org.msarpong.splash.service.mapping.photos.PhotoResponse
import org.msarpong.splash.service.mapping.profile.Profile
import org.msarpong.splash.service.mapping.search.SearchResponse
import org.msarpong.splash.service.mapping.search.collections.SearchCollectionResponse
import org.msarpong.splash.service.mapping.search.users.SearchUserResponse
import org.msarpong.splash.ui.settings.EditUser
import org.msarpong.splash.util.CLIENT_ID
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.*

sealed class ServiceResult {
    data class Error(val error: Throwable) : ServiceResult()
    data class Success(val pictureList: PhotoResponse) : ServiceResult()
    data class SuccessCollection(val collectionList: Collection) : ServiceResult()
    data class SuccessProfile(val profile: Profile) : ServiceResult()
    data class SuccessUser(val user: UserResponse) : ServiceResult()
    data class SuccessSearch(val search: SearchResponse) : ServiceResult()
    data class SuccessResultUser(val search: SearchUserResponse) : ServiceResult()
    data class SuccessResultLikePhoto(val likedPhoto: PhotoResponse) : ServiceResult()
    data class SuccessResultCollection(val search: SearchCollectionResponse) : ServiceResult()
    data class SuccessResultFollowing(val following: FollowingResponse) : ServiceResult()
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

    fun searchPhotos(query: String, receiver: ServiceReceiver) {
        Log.d("searchPhotos", "show: $query")

        val queryResult = service.getSearch(query)
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
                    Log.d("onResponse_getSearchPh", "showSuccess: $success")
                } else {
                    Log.d("onResponse_getSearchPh", "showError: $error")
                }
            }
        }))
    }

    fun searchUsers(query: String, receiver: ServiceReceiver) {
        val queryResult = service.getSearchUser(query)
        queryResult.enqueue(object : Callback<SearchUserResponse> {
            override fun onFailure(call: Call<SearchUserResponse>, t: Throwable) {
                Log.d("onFailure_getUserS", "showError: $t")
            }

            override fun onResponse(
                call: Call<SearchUserResponse>,
                response: Response<SearchUserResponse>
            ) {
                val success = response.body()
                val error = response.errorBody()
                if (success != null) {
                    val userResult = response.body()!!
                    receiver.receive(ServiceResult.SuccessResultUser(userResult))
                    Log.d("onResponse_getUserS", "showSuccess: $success")
                } else {
                    Log.d("onResponse_getUserS", "showError: $error")
                }
            }
        })
    }

    fun searchCollections(query: String, receiver: ServiceReceiver) {
        val queryResult = service.getSearchCollection(query)
        queryResult.enqueue(object : Callback<SearchCollectionResponse> {
            override fun onFailure(call: Call<SearchCollectionResponse>, t: Throwable) {
                Log.d("onFailure_searchC", "showError: $t")
            }

            override fun onResponse(
                call: Call<SearchCollectionResponse>,
                response: Response<SearchCollectionResponse>
            ) {
                val success = response.body()
                val error = response.errorBody()
                if (success != null) {
                    val collectionResult = response.body()!!
                    receiver.receive(ServiceResult.SuccessResultCollection(collectionResult))

                    Log.d("onResponse_searchC", "showSuccess: $success")

                } else {
                    Log.d("onResponse_searchC", "showError: $error")
                }
            }
        })
    }

    fun getCurrentUser(authToken: String, receiver: ServiceReceiver) {
        val key = "Bearer "
        val token = key + authToken
        val userResult = service.getMe(token)
        Log.d("getCurrentUser", "Authorization: $token")

        userResult.enqueue(object : Callback<UserResponse> {
            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Log.d("onFailure_getCurUser", "showError: $t")
            }

            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                val success = response.body()
                val error = response.errorBody()
                if (success != null) {
                    val userInfo = response.body()!!
                    receiver.receive(ServiceResult.SuccessUser(userInfo))
                    Log.d("onResponse_getCurUser", "showSuccess: $success")
                } else {
                    Log.d("onResponse_getCurUser", "showError: $error")
                }
            }
        })
    }

    fun editCurrentUser(
        authToken: String,saveData: EditUser,receiver: ServiceReceiver
    ) {
        val key = "Bearer "
        val token = key + authToken
        val userResult = service.editMe(
            token,
            saveData.username,
            saveData.firstName,
            saveData.lastName,
            saveData.eMail,
            saveData.bio,
            saveData.instagram
        )

        Log.d("editCurrentUser", "Authorization: $token")

        userResult.enqueue(object : Callback<UserResponse> {
            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Log.d("onFailure_editUser", "showError: $t")
            }

            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                val success = response.body()
                val error = response.errorBody()
                if (success != null) {
                    val userInfo = response.body()!!
                    receiver.receive(ServiceResult.SuccessUser(userInfo))
                    Log.d("onResponse_editUser", "showSuccess: $success")
                } else {
                    Log.d("onResponse_editUser", "showError: $error")
                }
            }

        })
    }

    fun getFollowing(username: String, receiver: ServiceReceiver) {

        val following = service.getUserFollowing(username)
        following.enqueue(object : Callback<FollowingResponse> {
            override fun onFailure(call: Call<FollowingResponse>, t: Throwable) {
                Log.d("onFailure_getFollowing", "showError: $t")
            }

            override fun onResponse(
                call: Call<FollowingResponse>,
                response: Response<FollowingResponse>
            ) {
                val success = response.body()
                val error = response.errorBody()
                if (success != null) {
                    val followingInfo = response.body()!!
                    receiver.receive(ServiceResult.SuccessResultFollowing(followingInfo))
                    Log.d("onResponse_getFollowing", "showSuccess: $success")
                } else {
                    Log.d("onResponse_getFollowing", "showError: $error")
                }
            }
        })
    }

    fun getLikePhoto(username: String, receiver: ServiceReceiver) {
        val liked = service.getUserLikedPhoto(username)
        liked.enqueue(object : Callback<PhotoResponse> {
            override fun onFailure(call: Call<PhotoResponse>, t: Throwable) {
                Log.d("onFailure_getLikePhoto", "showError: $t")
            }

            override fun onResponse(call: Call<PhotoResponse>, response: Response<PhotoResponse>) {
                val success = response.body()
                val error = response.errorBody()
                if (success != null) {
                    val likedPhoto = response.body()!!
                    receiver.receive(ServiceResult.SuccessResultLikePhoto(likedPhoto))
                    Log.d("onResponse_getLikePhoto", "showSuccess: $success")
                } else {
                    Log.d("onResponse_getLikePhoto", "showError: $error")
                }
            }
        })
    }

    fun getUserCollection(username: String, receiver: ServiceReceiver) {
        val collection = service.getUserCollection(username)
        collection.enqueue(object : Callback<Collection> {
            override fun onFailure(call: Call<Collection>, t: Throwable) {
                Log.d("onFailure_getUserCol", "showError: $t")
            }

            override fun onResponse(call: Call<Collection>, response: Response<Collection>) {
                val success = response.body()
                val error = response.errorBody()

                if (success != null) {
                    val pictureResult = response.body()!!
                    receiver.receive(ServiceResult.SuccessCollection(pictureResult))
                    Log.d("onResponse_getUserCol", "showSuccess: $success")
                } else {
                    Log.d("onResponse_getUserCol", "showError: $error")
                }
            }
        })
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

    @GET("collections/featured")
    fun getPhotoCollections(
        @Query("client_id") client_id: String = CLIENT_ID
    ): Call<Collection>

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

    @GET("/search/photos")
    fun getSearch(
        @Query("query") query: String,
        @Query("client_id") client_id: String = CLIENT_ID
    ): Call<SearchResponse>

    @GET("/search/users")
    fun getSearchUser(
        @Query("query") query: String,
        @Query("client_id") client_id: String = CLIENT_ID
    ): Call<SearchUserResponse>

    @GET("/search/collections")
    fun getSearchCollection(
        @Query("query") query: String,
        @Query("client_id") client_id: String = CLIENT_ID
    ): Call<SearchCollectionResponse>

    @GET("/me")
    fun getMe(
        @Header("Authorization") authorization: String
    ): Call<UserResponse>

    @PUT("/me")
    fun editMe(
        @Header("Authorization") authorization: String,
        @Query("username") username: String,
        @Query("first_name") first_name: String,
        @Query("last_name") last_name: String,
        @Query("email") email: String,
        @Query("bio") bio: String,
        @Query("instagram_username") instagram_username: String

    ): Call<UserResponse>

    @GET("/users/{username}/following/")
    fun getUserFollowing(
        @Path("username") username: String,
        @Query("client_id") client_id: String = CLIENT_ID
    ): Call<FollowingResponse>

    @GET("/users/{username}/likes/")
    fun getUserLikedPhoto(
        @Path("username") username: String,
        @Query("client_id") client_id: String = CLIENT_ID
    ): Call<PhotoResponse>

    @GET("/users/{username}/collections/")
    fun getUserCollection(
        @Path("username") username: String,
        @Query("client_id") client_id: String = CLIENT_ID
    ): Call<Collection>


}

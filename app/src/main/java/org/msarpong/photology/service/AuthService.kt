package org.msarpong.photology.service

import android.util.Log
import org.msarpong.photology.di.authRetrofit
import org.msarpong.photology.service.mapping.auth.AuthResponse
import org.msarpong.photology.util.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Query

sealed class AuthServiceResult {
    data class Error(val error: Throwable) : AuthServiceResult()
    data class Success(val authResult: AuthResponse) : AuthServiceResult()
}

interface AuthServiceReceiver {
    fun receive(result: AuthServiceResult)
}

class AuthService {
    private val service: AuthServiceApi = authRetrofit.create(
        AuthServiceApi::class.java
    )

    fun authUser(code: String, receiver: AuthServiceReceiver) {
        Log.d("authUser", code)

        val result = service.getAuthUser(code)
        result.enqueue(object : Callback<AuthResponse> {
            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                Log.d("onFailure_authUser", "showError: $t")
            }

            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                val success = response.body()
                val error = response.errorBody()

                if (success != null) {
                    val authResult = response.body()!!

                    receiver.receive(AuthServiceResult.Success(authResult))

                    Log.d("onResponse_authUser", "showSuccess: $success")
                } else {
                    Log.d("onResponse_authUser", "showError: $error")
                }
            }
        })
    }
}

interface AuthServiceApi {

    @POST("token/")
    fun getAuthUser(
        @Query("code") code: String,
        @Query("client_id") client_id: String = CLIENT_ID,
        @Query("client_secret") client_secret: String = CLIENT_SECRET,
        @Query("redirect_uri") redirect_uri: String = REDIRECT_URI,
        @Query("scope") scope: String = SCOPE,
        @Query("grant_type") grant_type: String = GRANT_TYPE
    ): Call<AuthResponse>
}

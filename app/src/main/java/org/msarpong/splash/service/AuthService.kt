package org.msarpong.splash.service

import android.util.Log
import org.msarpong.splash.di.authRetrofit
import org.msarpong.splash.service.mapping.auth.AuthResponse
import org.msarpong.splash.util.CLIENT_ID
import org.msarpong.splash.util.CLIENT_SECRET
import org.msarpong.splash.util.GRANT_TYPE
import org.msarpong.splash.util.REDIRECT_URI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.*

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

        val result = service.getAuthUser(code, code)
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
        @Query("grant_type") grant_type: String = GRANT_TYPE
    ): Call<AuthResponse>
}

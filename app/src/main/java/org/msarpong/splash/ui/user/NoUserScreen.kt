package org.msarpong.splash.ui.user

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Dialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import org.koin.android.ext.android.inject
import org.msarpong.splash.R
import org.msarpong.splash.service.mapping.auth.AuthResponse
import org.msarpong.splash.service.mapping.auth.user.UserResponse
import org.msarpong.splash.ui.main.MainScreen
import org.msarpong.splash.ui.welcome.WelcomeEvent
import org.msarpong.splash.ui.welcome.WelcomeState
import org.msarpong.splash.ui.welcome.WelcomeViewModel
import org.msarpong.splash.util.*
import org.msarpong.splash.util.sharedpreferences.KeyValueStorage

class NoUserScreen : AppCompatActivity() {

    private val viewModel: WelcomeViewModel by inject()
    private val prefs: KeyValueStorage by inject()

    private lateinit var signInEmailButton: Button
    private lateinit var signInDialog: Dialog
    private lateinit var tokenAccess: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.no_user)
        initViews()
        setupViews()
        setupObserver()
    }

    private fun initViews() {
        signInEmailButton = findViewById(R.id.welcome_sign_in_email)
    }

    private fun setupViews() {
        signInEmailButton.setOnClickListener {
            checkUser()
        }
    }

    private fun checkUser() {
        if (prefs.getString(ACCESS_TOKEN).isNullOrEmpty()) {
            setupWebViewDialog(url)
        } else {
            Log.d("checkUser", tokenAccess)
            startActivity(Intent(this, MainScreen::class.java))
            finish()
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    fun setupWebViewDialog(url: String) {
        signInDialog = Dialog(this)
        val webView = WebView(this)
        webView.isVerticalScrollBarEnabled = false
        webView.isHorizontalScrollBarEnabled = false
        webView.settings.javaScriptEnabled = true
        webView.webViewClient = SignInWebViewClient()
        webView.loadUrl(url)
        signInDialog.setContentView(webView)
        signInDialog.show()
    }

    inner class SignInWebViewClient : WebViewClient() {
        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            if (request!!.url.toString().startsWith(AUTH_URL)) {
                val code = handleUrl(request.url.toString())

                if (request.url.toString().contains("code=")) {
                    Log.d("SignInWebViewClient", code)
                    viewModel.send(WelcomeEvent.Load(code))
                }
                return true
            }
            return false
        }
    }

    private fun setupObserver() {
        viewModel.state.observe(this, Observer { state ->
            when (state) {
                is WelcomeState.Error -> showError(state.error)
                is WelcomeState.Success -> saveAuth(state.authResult)
                is WelcomeState.SuccessUser -> saveUser(state.user)
            }
        })
    }

    private fun saveAuth(authResult: AuthResponse) {
        Log.d("saveAuth", authResult.accessToken)
        prefs.putString(ACCESS_TOKEN, authResult.accessToken)
        viewModel.send(WelcomeEvent.LoadUser(authResult.accessToken))

    }

    private fun saveUser(user: UserResponse) {
        Log.d("saveUser", user.toString())
        prefs.putString(USERNAME, user.username)
        prefs.putString(ID_USER, user.id)
        prefs.putBoolean(IS_LOGGED, true)
        startActivity(Intent(this, MainScreen::class.java))
        finish()
    }

    private fun showError(error: Throwable) {
        Log.d("WelcomeScreen", "showError: $error")
    }
}
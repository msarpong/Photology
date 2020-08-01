package org.msarpong.splash.ui.welcome

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
import org.msarpong.splash.util.*
import org.msarpong.splash.util.sharedpreferences.KeyValueStorage


class WelcomeScreen : AppCompatActivity() {

    private val viewModel: WelcomeViewModel by inject()
    private val prefs: KeyValueStorage by inject()

    private lateinit var skipButton: Button
    private lateinit var signInEmailButton: Button
    private lateinit var signInDialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.welcome_screen)
        initViews()
        setupViews()
        setupObserver()
    }

    private fun checkUser() {
        val token: String? = prefs.getString(ACCESS_TOKEN)
        if (prefs.getString(ACCESS_TOKEN).isNullOrEmpty()) {
            Log.d("checkUserIf", token.toString())
            setupWebViewDialog(url)
        } else {
            Log.d("checkUserElse", token)
            val main = Intent(this, MainScreen::class.java)
            startActivity(main)
            finish()
        }
    }

    private fun initViews() {
        skipButton = findViewById(R.id.skip_button)
        signInEmailButton = findViewById(R.id.welcome_sign_in_email)
    }

    private fun setupViews() {
        skipButton.setOnClickListener {
            val main = Intent(this, MainScreen::class.java)
            startActivity(main)
            finish()
        }
        signInEmailButton.setOnClickListener {
            checkUser()
        }
    }

    private fun setupObserver() {
        viewModel.state.observe(this, Observer { state ->
            when (state) {
                is WelcomeState.Error -> showError(state.error)
                is WelcomeState.Success -> saveAuth(state.authResult)
                is WelcomeState.SuccessUser -> saveUser(state.userResult)
            }
        })
    }

    private fun saveAuth(authResult: AuthResponse) {
        signInDialog.dismiss()
        Log.d("saveUser", authResult.accessToken)
        prefs.putString(ACCESS_TOKEN, authResult.accessToken)
        prefs.putString(ACCESS_SCOPE, authResult.scope)
        startActivity(Intent(this, MainScreen::class.java))
        finish()
    }

    private fun saveUser(userResult: UserResponse) {
        prefs.putString(USERNAME, userResult.username)
        prefs.putString(ID_USER, userResult.id)
    }

    private fun showError(error: Throwable) {
        Log.d("WelcomeScreen", "showError: $error")
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
        signInDialog?.window?.setBackgroundDrawableResource(R.drawable.background_dialog)
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
                    viewModel.send(WelcomeEvent.LoadMe(code))
                }
                return true
            }
            return false
        }
    }
}

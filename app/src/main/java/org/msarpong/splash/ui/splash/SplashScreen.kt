package org.msarpong.splash.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import org.koin.android.ext.android.inject
import org.msarpong.splash.R
import org.msarpong.splash.ui.main.MainScreen
import org.msarpong.splash.ui.welcome.WelcomeScreen
import org.msarpong.splash.util.IS_LOGGED
import org.msarpong.splash.util.sharedpreferences.KeyValueStorage

const val WELCOME_TIME = 1000L

class SplashScreen : AppCompatActivity() {

    private val prefs: KeyValueStorage by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)
        if (prefs.getBoolean(IS_LOGGED, false)) {
            goToMain()
        } else {
            goToWelcome()
        }
    }

    private fun goToMain() {
        val runnableMain = Runnable {
            startActivity(Intent(this, MainScreen::class.java))
            finish()
        }
        Handler().postDelayed(runnableMain, WELCOME_TIME)
    }

    private fun goToWelcome() {
        val runnableWelcome = Runnable {
            startActivity(Intent(this, WelcomeScreen::class.java))
            finish()
        }
        Handler().postDelayed(runnableWelcome, WELCOME_TIME)
    }
}
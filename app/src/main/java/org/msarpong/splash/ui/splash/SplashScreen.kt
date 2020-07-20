package org.msarpong.splash.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import org.msarpong.splash.R
import org.msarpong.splash.ui.welcome.WelcomeScreen

const val WELCOME_TIME = 4000L

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)
        goToWelcome()
    }

    private fun goToWelcome() {
        val runnableWelcome = Runnable {
            val main = Intent(this, WelcomeScreen::class.java)
            startActivity(main)
            finish()
        }
        Handler().postDelayed(runnableWelcome, WELCOME_TIME)

    }
}
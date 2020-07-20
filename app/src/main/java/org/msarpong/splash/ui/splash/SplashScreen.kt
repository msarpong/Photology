package org.msarpong.splash.ui.splash

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.msarpong.splash.R

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)
    }
}
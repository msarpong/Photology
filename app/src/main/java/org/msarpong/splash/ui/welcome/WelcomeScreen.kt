package org.msarpong.splash.ui.welcome

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import org.msarpong.splash.R
import org.msarpong.splash.ui.main.MainScreen

class WelcomeScreen : AppCompatActivity() {

    private lateinit var skipButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.welcome_screen)

        setupViews()
    }

    private fun setupViews() {
        skipButton = findViewById(R.id.skip_button)
        skipButton.setOnClickListener {
            val main = Intent(this, MainScreen::class.java)
            startActivity(main)
            finish()
        }
    }
}
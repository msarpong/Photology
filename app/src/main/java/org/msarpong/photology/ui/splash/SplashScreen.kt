package org.msarpong.photology.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import org.koin.android.ext.android.inject
import org.msarpong.photology.R
import org.msarpong.photology.service.mapping.detail_photo.DetailPhotoResponse
import org.msarpong.photology.ui.main.MainScreen
import org.msarpong.photology.ui.welcome.WelcomeScreen
import org.msarpong.photology.util.ConnectionType
import org.msarpong.photology.util.IS_LOGGED
import org.msarpong.photology.util.NetworkMonitorUtil
import org.msarpong.photology.util.sharedpreferences.KeyValueStorage

const val WELCOME_TIME = 5000L

class SplashScreen : AppCompatActivity() {

    private val prefs: KeyValueStorage by inject()
    private val networkMonitor = NetworkMonitorUtil(this)
    private val viewModel: SplashViewModel by inject()

    private lateinit var imageSplash: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)
        initViews()
        checkConnection()

        if (prefs.getBoolean(IS_LOGGED, false)) {
            goToMain()
        } else {
            goToWelcome()
        }
    }

    fun initViews() {
        imageSplash = findViewById(R.id.splash_img)
    }

    override fun onStart() {
        super.onStart()
        viewModel.state.observe(this, Observer { state ->
            when (state) {
                is SplashState.Success -> {
                    showPhotos(state.photoList)
                }
            }
        })
        viewModel.send(SplashEvent.Load("dark grey"))
    }

    private fun showPhotos(photoList: DetailPhotoResponse) {
        Log.i("SplashScreen", "showPhotos: $photoList.urls.regular")
        Glide
            .with(imageSplash.context)
            .load(photoList.urls.regular)
            .fitCenter()
            .into(imageSplash)
    }

    override fun onResume() {
        super.onResume()
        networkMonitor.register()
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

    private fun checkConnection() {
        networkMonitor.result = { isAvailable, type ->
            runOnUiThread {
                when (isAvailable) {
                    true -> {
                        when (type) {
                            ConnectionType.Wifi -> {
                                Log.i("NETWORK_MONITOR_STATUS", "Wifi Connection")
                            }
                            ConnectionType.Cellular -> {
                                Log.i("NETWORK_MONITOR_STATUS", "Cellular Connection")
                            }
                            else -> {
                            }
                        }
                    }
                    false -> {
                        Log.i("NETWORK_MONITOR_STATUS", "No Connection")
                    }
                }
            }
        }
    }
}

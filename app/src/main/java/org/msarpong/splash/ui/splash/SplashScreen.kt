package org.msarpong.splash.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import org.koin.android.ext.android.inject
import org.msarpong.splash.R
import org.msarpong.splash.ui.main.MainScreen
import org.msarpong.splash.ui.welcome.WelcomeScreen
import org.msarpong.splash.util.ConnectionType
import org.msarpong.splash.util.IS_LOGGED
import org.msarpong.splash.util.NetworkMonitorUtil
import org.msarpong.splash.util.sharedpreferences.KeyValueStorage

const val WELCOME_TIME = 1000L

class SplashScreen : AppCompatActivity() {

    private val prefs: KeyValueStorage by inject()
    private val networkMonitor = NetworkMonitorUtil(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)
        checkConnection()

        if (prefs.getBoolean(IS_LOGGED, false)) {
            goToMain()
        } else {
            goToWelcome()
        }
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
}
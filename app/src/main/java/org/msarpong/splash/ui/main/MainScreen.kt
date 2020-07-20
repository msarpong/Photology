package org.msarpong.splash.ui.main

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import org.koin.android.ext.android.inject
import org.msarpong.splash.R
import org.msarpong.splash.service.mapping.Unsplash


class MainScreen : AppCompatActivity() {

    private val viewModel: MainViewModel by inject()

    private lateinit var progressBar: ProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupViews()

    }

    private fun setupViews() {
        progressBar = findViewById(R.id.progressBar)

    }

    override fun onStart() {
        super.onStart()
        viewModel.state.observe(this, Observer { state ->
            when (state) {
                is MainState.InProgress -> showProgress()
                is MainState.Error -> {
                    hideProgress()
                    showError(state.error)
                }
                is MainState.Success -> {
                    hideProgress()
                    showPhotos(state.pictureList)
                }
            }
        })
        viewModel.send(MainEvent.Load)
    }

    private fun showPhotos(response: Unsplash) {
        Log.d("MainActivity", "showPhotos:$response")

    }

    private fun showProgress() {
        progressBar.visibility = View.VISIBLE
    }

    private fun hideProgress() {
        progressBar.visibility = View.GONE
    }

    private fun showError(error: Throwable) {
        Log.d("MainActivity", "showError: $error")
    }
}

package org.msarpong.splash.ui.main

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.koin.android.ext.android.inject
import org.msarpong.splash.R
import org.msarpong.splash.service.mapping.Unsplash
import org.msarpong.splash.service.mapping.UnsplashItem


class MainScreen : AppCompatActivity() {

    private val viewModel: MainViewModel by inject()

    private lateinit var progressBar: ProgressBar
    private lateinit var homeBtn: Button
    private lateinit var collectionBtn: Button
    private lateinit var searchBtn: Button
    private lateinit var profileBtn: Button

    private lateinit var imageRV: RecyclerView
    private lateinit var imageAdapter: ListAdapter<UnsplashItem, UnsplashViewHolder>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupViews()
    }

    private fun setupViews() {
        progressBar = findViewById(R.id.progressBar)

        homeBtn = findViewById(R.id.home_btn)
        collectionBtn = findViewById(R.id.collection_btn)
        searchBtn = findViewById(R.id.search_btn)
        profileBtn = findViewById(R.id.profile_btn)

        imageRV = findViewById(R.id.main_image)
        imageRV.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        imageAdapter = MainAdapter()
        imageRV.adapter = imageAdapter
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
        imageAdapter.submitList(response)
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

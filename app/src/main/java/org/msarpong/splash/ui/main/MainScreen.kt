package org.msarpong.splash.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.koin.android.ext.android.inject
import org.msarpong.splash.R
import org.msarpong.splash.service.mapping.photos.PhotoResponse
import org.msarpong.splash.service.mapping.photos.PhotoResponseItem
import org.msarpong.splash.ui.collections.CollectionScreen
import org.msarpong.splash.ui.following.FollowingScreen
import org.msarpong.splash.ui.search.SearchPhotoScreen
import org.msarpong.splash.ui.settings.SettingsScreen
import org.msarpong.splash.ui.user.UserScreen
import org.msarpong.splash.util.ACCESS_TOKEN
import org.msarpong.splash.util.sharedpreferences.KeyValueStorage

class MainScreen : AppCompatActivity() {

    private val viewModel: MainViewModel by inject()
    private val prefs: KeyValueStorage by inject()

    private lateinit var progressBar: ProgressBar

    private lateinit var settingBtn: ImageButton
    private lateinit var homeBtn: ImageButton
    private lateinit var collectionBtn: ImageButton
    private lateinit var searchBtn: ImageButton
    private lateinit var profileBtn: ImageButton
    private lateinit var editorialBtn: Button
    private lateinit var followingBtn: Button
    private lateinit var imageRV: RecyclerView
    private lateinit var imageAdapter: ListAdapter<PhotoResponseItem, UnsplashViewHolder>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
        setupViews()
    }

    private fun initViews() {
        progressBar = findViewById(R.id.progressBar)
        settingBtn = findViewById(R.id.setting_btn)
        homeBtn = findViewById(R.id.home_btn)
        collectionBtn = findViewById(R.id.collection_btn)
        searchBtn = findViewById(R.id.search_btn)
        profileBtn = findViewById(R.id.profile_btn)
        editorialBtn = findViewById(R.id.editorial_btn)
        followingBtn = findViewById(R.id.following_btn)
        imageRV = findViewById(R.id.main_image)
        imageRV.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        imageAdapter = MainAdapter()
        imageRV.adapter = imageAdapter
    }

    private fun setupViews() {

        if (!prefs.getString(ACCESS_TOKEN).isNullOrEmpty()) {
            settingBtn.visibility = View.VISIBLE
        }

        followingBtn.setCompoundDrawables(null, null, null, null)

        homeBtn.setColorFilter(ContextCompat.getColor(this, R.color.active_button))
        collectionBtn.setOnClickListener {
            startActivity(Intent(this, CollectionScreen::class.java))
        }
        searchBtn.setOnClickListener {
            startActivity(Intent(this, SearchPhotoScreen::class.java))
        }
        profileBtn.setOnClickListener {
            startActivity(Intent(this, UserScreen::class.java))
        }
        followingBtn.setOnClickListener {
            startActivity(Intent(this, FollowingScreen::class.java))
        }
        settingBtn.setOnClickListener {
            startActivity(Intent(this, SettingsScreen::class.java))
        }
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

    private fun showPhotos(response: PhotoResponse) {
//        val newList = response.toList()
//        imageAdapter.submitList(newList.sortedBy { it.id })
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

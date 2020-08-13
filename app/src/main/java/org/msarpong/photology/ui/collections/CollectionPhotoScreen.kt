package org.msarpong.photology.ui.collections

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.koin.android.ext.android.inject
import org.msarpong.photology.R
import org.msarpong.photology.service.mapping.photos.PhotoResponse
import org.msarpong.photology.service.mapping.photos.PhotoResponseItem
import org.msarpong.photology.ui.main.MainAdapter
import org.msarpong.photology.ui.main.UnsplashViewHolder
import org.msarpong.photology.ui.search.SearchPhotoScreen
import org.msarpong.photology.ui.settings.SettingsScreen
import org.msarpong.photology.ui.user.UserScreen
import org.msarpong.photology.util.ACCESS_TOKEN
import org.msarpong.photology.util.sharedpreferences.KeyValueStorage

private const val BUNDLE_ID: String = "BUNDLE_ID"

class CollectionPhotoScreen : AppCompatActivity() {

    private val viewModel: CollectionViewModel by inject()
    private val prefs: KeyValueStorage by inject()

    private lateinit var detailId: String

    private lateinit var progressBar: ProgressBar
    private lateinit var settingBtn: ImageButton
    private lateinit var homeBtn: ImageButton
    private lateinit var collectionBtn: ImageButton
    private lateinit var searchBtn: ImageButton
    private lateinit var profileBtn: ImageButton
    private lateinit var imageRV: RecyclerView
    private lateinit var imageAdapter: ListAdapter<PhotoResponseItem, UnsplashViewHolder>

    companion object {
        fun openPhotoCollection(startingActivity: Activity, detailId: String) {
            val intent = Intent(startingActivity, CollectionPhotoScreen::class.java)
                .putExtra(BUNDLE_ID, detailId)
            Log.d("openPhotoCollection", detailId)
            startingActivity.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.collection_photo_screen)
        initViews()
        setupViews()
    }

    private fun initViews() {
        detailId = intent.getStringExtra(BUNDLE_ID)
        progressBar = findViewById(R.id.progressBar)
        settingBtn = findViewById(R.id.setting_btn)
        homeBtn = findViewById(R.id.home_btn)
        collectionBtn = findViewById(R.id.collection_btn)
        searchBtn = findViewById(R.id.search_btn)
        profileBtn = findViewById(R.id.profile_btn)
        imageRV = findViewById(R.id.main_image)
        imageRV.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        imageAdapter = MainAdapter()
        imageRV.adapter = imageAdapter
    }

    private fun setupViews() {

        if (!prefs.getString(ACCESS_TOKEN).isNullOrEmpty()) {
            settingBtn.visibility = View.VISIBLE
        }

        collectionBtn.setColorFilter(ContextCompat.getColor(this, R.color.active_button))
        collectionBtn.setOnClickListener {
            startActivity(Intent(this, CollectionScreen::class.java))
        }
        searchBtn.setOnClickListener {
            startActivity(Intent(this, SearchPhotoScreen::class.java))
        }
        profileBtn.setOnClickListener {
            startActivity(Intent(this, UserScreen::class.java))
        }

        settingBtn.setOnClickListener {
            startActivity(Intent(this, SettingsScreen::class.java))
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.state.observe(this, Observer { state ->
            when (state) {
                is CollectionState.InProgress -> showProgress()
                is CollectionState.SuccessPhoto -> {
                    hideProgress()
                    showPhotos(state.photoList)
                }
                is CollectionState.Error -> {
                    hideProgress()
                    showError(state.error)
                }
            }
        })
        viewModel.send(CollectionEvent.LoadPhoto(detailId))

    }

    private fun showPhotos(response: PhotoResponse) {
        imageAdapter.submitList(response)
        Log.d("CollectionPhotoScreen", "showPhotos:$response")
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
package org.msarpong.splash.ui.detail_photo

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import org.koin.android.ext.android.inject
import org.msarpong.splash.R
import org.msarpong.splash.service.mapping.UnsplashItem
import org.msarpong.splash.ui.collections.CollectionScreen
import org.msarpong.splash.ui.main.MainScreen
import org.msarpong.splash.ui.search.SearchScreen
import org.msarpong.splash.ui.setting.SettingScreen

private const val BUNDLE_ID: String = "BUNDLE_ID"

class DetailPhotoScreen : AppCompatActivity() {

    private val viewModel: DetailPhotoViewModel by inject()

    private lateinit var progressBar: ProgressBar

    private lateinit var detailImage: ImageView
    private lateinit var detailId: String
    private lateinit var detailUserImage: ImageView
    private lateinit var detailUser: TextView
    private lateinit var detailUserName: TextView

    private lateinit var homeBtn: ImageButton
    private lateinit var collectionBtn: ImageButton
    private lateinit var searchBtn: ImageButton
    private lateinit var profileBtn: ImageButton

    companion object {
        fun openDetailPhoto(startingActivity: Activity, detailId: String) {
            val intent = Intent(startingActivity, DetailPhotoScreen::class.java)
                .putExtra(BUNDLE_ID, detailId)
            Log.d("openDetailPicture", detailId)
            startingActivity.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_photo_screen)
        setupViews()

    }

    private fun setupViews() {
        progressBar = findViewById(R.id.progressBar)

        homeBtn = findViewById(R.id.home_btn)
        collectionBtn = findViewById(R.id.collection_btn)
        searchBtn = findViewById(R.id.search_btn)
        profileBtn = findViewById(R.id.profile_btn)
        detailImage = findViewById(R.id.detail_image)
        detailUserImage = findViewById(R.id.detail_image_user)
        detailUser = findViewById(R.id.detail_text_name)
        detailUserName = findViewById(R.id.detail_text_username)

        detailId = intent.getStringExtra(BUNDLE_ID)

        homeBtn.setOnClickListener {
            startActivity(Intent(this, MainScreen::class.java))
        }
        collectionBtn.setOnClickListener {
            startActivity(Intent(this, CollectionScreen::class.java))
        }
        searchBtn.setOnClickListener {
            startActivity(Intent(this, SearchScreen::class.java))
        }
        profileBtn.setOnClickListener {
            startActivity(Intent(this, SettingScreen::class.java))
        }

        Log.d("setupViews", "detailId: $detailId")
    }

    override fun onStart() {
        super.onStart()
        viewModel.state.observe(this, Observer { state ->
            when (state) {
                is DetailState.InProgress -> showProgress()
                is DetailState.Error -> {
                    hideProgress()
                    showError(state.error)
                }
                is DetailState.Success -> {
                    hideProgress()
                    showPhoto(state.pictureDetail)
                }
            }
        })
        viewModel.send(DetailEvent.Load(detailId))
        Log.d("onStart", detailId)
    }

    private fun showPhoto(response: UnsplashItem) {
        Glide
            .with(detailImage.context)
            .load(response.urls.regular)
            .fitCenter()
            .into(detailImage)

        Glide
            .with(detailUser.context)
            .load(response.user.profileImage.medium)
            .fitCenter()
            .into(detailUserImage)

        detailUser.text = response.user.name
        detailUserName.text = response.user.username


        Log.d("DetailPhotoScreen", "showDetailPhoto:$response")
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
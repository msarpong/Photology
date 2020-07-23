package org.msarpong.splash.ui.profile

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import org.koin.android.ext.android.inject
import org.msarpong.splash.R
import org.msarpong.splash.service.mapping.UnsplashItem
import org.msarpong.splash.service.mapping.profile.Profile
import org.msarpong.splash.ui.collections.CollectionScreen
import org.msarpong.splash.ui.detail_photo.DetailEvent
import org.msarpong.splash.ui.detail_photo.DetailPhotoScreen
import org.msarpong.splash.ui.detail_photo.DetailPhotoViewModel
import org.msarpong.splash.ui.main.MainScreen
import org.msarpong.splash.ui.search.SearchScreen
import org.msarpong.splash.ui.setting.SettingScreen

private const val BUNDLE_ID: String = "BUNDLE_ID"

class ProfileScreen : AppCompatActivity() {

    private val viewModel: ProfileViewModel by inject()

    private lateinit var username: String

    private lateinit var progressBar: ProgressBar

    private lateinit var homeBtn: ImageButton
    private lateinit var collectionBtn: ImageButton
    private lateinit var searchBtn: ImageButton
    private lateinit var profileBtn: ImageButton

    private lateinit var profileImage: ImageView
    private lateinit var profileUsername: TextView
    private lateinit var profileFullName: TextView
    private lateinit var profileBio: TextView

    companion object {
        fun openProfile(startingActivity: Activity, detailId: String) {
            val intent =
                Intent(startingActivity, ProfileScreen::class.java).putExtra(BUNDLE_ID, detailId)
            Log.d("openProfile", detailId)
            startingActivity.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_screen)
        setupViews()
    }

    private fun setupViews() {
        username = intent.getStringExtra(BUNDLE_ID)

        progressBar = findViewById(R.id.progressBar)

        homeBtn = findViewById(R.id.home_btn)
        collectionBtn = findViewById(R.id.collection_btn)
        searchBtn = findViewById(R.id.search_btn)
        profileBtn = findViewById(R.id.profile_btn)

        profileImage = findViewById(R.id.profile_image)
        profileUsername = findViewById(R.id.profile_text_username)
        profileFullName = findViewById(R.id.profile_text_fullname)
        profileBio = findViewById(R.id.profile_text_bio)

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

        Log.d("setupViews", "username: $username")

    }

    override fun onStart() {
        super.onStart()

        viewModel.state.observe(this, Observer { state ->
            when (state) {
                is ProfileState.Error -> {
                    hideProgress()
                    showError(state.error)
                }
                is ProfileState.InProgress -> showProgress()
                is ProfileState.Success -> {
                    hideProgress()
                    showProfile(state.pictureList)
                }
            }

        })
        viewModel.send(ProfileEvent.Load(username))
        Log.d("onStartProfile", username)

    }

    private fun showProfile(response: Profile) {
        Log.d("ProfileScreen", "showProfile:${response.username}")
        profileImage = findViewById(R.id.profile_image)
        profileUsername = findViewById(R.id.profile_text_username)
        profileFullName = findViewById(R.id.profile_text_fullname)
        profileBio = findViewById(R.id.profile_text_bio)

        Glide
            .with(profileImage.context)
            .load(response.profileImage.large)
            .fitCenter()
            .into(profileImage)

        profileUsername.text = response.username
        profileFullName.text = response.name

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
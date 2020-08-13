package org.msarpong.photology.ui.profile

import android.app.Activity
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.koin.android.ext.android.inject
import org.msarpong.photology.R
import org.msarpong.photology.service.mapping.photos.PhotoResponse
import org.msarpong.photology.service.mapping.photos.PhotoResponseItem
import org.msarpong.photology.service.mapping.profile.Profile
import org.msarpong.photology.ui.collections.CollectionScreen
import org.msarpong.photology.ui.main.MainAdapter
import org.msarpong.photology.ui.main.MainScreen
import org.msarpong.photology.ui.main.UnsplashViewHolder
import org.msarpong.photology.ui.search.SearchPhotoScreen
import org.msarpong.photology.ui.user.UserScreen

private const val BUNDLE_ID: String = "BUNDLE_ID"

class ProfileLikeScreen : AppCompatActivity() {

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
    private lateinit var profilePhotoBtn: Button
    private lateinit var profileLikeBtn: Button
    private lateinit var profileCollectionBtn: Button
    private lateinit var noDataLabel: TextView

    private lateinit var imageRV: RecyclerView
    private lateinit var imageAdapter: ListAdapter<PhotoResponseItem, UnsplashViewHolder>

    companion object {
        fun openLikeProfile(startingActivity: Activity, detailId: String) {
            val intent =
                Intent(startingActivity, ProfileLikeScreen::class.java).putExtra(
                    BUNDLE_ID,
                    detailId
                )
            startingActivity.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_like_screen)
        initViews()
        setupViews()
    }

    private fun initViews() {
        username = intent.getStringExtra(BUNDLE_ID)
        progressBar = findViewById(R.id.progressBar)
        homeBtn = findViewById(R.id.home_btn)
        collectionBtn = findViewById(R.id.collection_btn)
        searchBtn = findViewById(R.id.search_btn)
        profileBtn = findViewById(R.id.profile_btn)
        profileImage = findViewById(R.id.profile_image)
        profileUsername = findViewById(R.id.profile_text_username)
        profileFullName = findViewById(R.id.profile_text_full_name)
        profilePhotoBtn = findViewById(R.id.profile_photo)
        profileLikeBtn = findViewById(R.id.profile_like)
        profileLikeBtn.typeface = Typeface.DEFAULT_BOLD
        profileCollectionBtn = findViewById(R.id.profile_collection)
        noDataLabel = findViewById(R.id.no_data_text)

        imageRV = findViewById(R.id.main_image)
        imageRV.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        imageAdapter = MainAdapter()
        imageRV.adapter = imageAdapter

    }

    private fun setupViews() {
        profileLikeBtn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16F)

        homeBtn.setOnClickListener {
            startActivity(Intent(this, MainScreen::class.java))
        }
        collectionBtn.setOnClickListener {
            startActivity(Intent(this, CollectionScreen::class.java))
        }
        searchBtn.setOnClickListener {
            startActivity(Intent(this, SearchPhotoScreen::class.java))
        }
        profileBtn.setOnClickListener {
            startActivity(Intent(this, UserScreen::class.java))
        }

        profilePhotoBtn.setOnClickListener {
            ProfilePhotoScreen.openPhotoProfile(this, username)
        }
        profileLikeBtn.setOnClickListener {
            openLikeProfile(this, username)
        }
        profileCollectionBtn.setOnClickListener {
            ProfileCollectionScreen.openCollectionProfile(this, username)
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

                is ProfileState.SuccessPhoto -> {
                    hideProgress()
                }
                is ProfileState.SuccessLikePhoto ->{
                    showLikedPhoto(state.pictureList)
                }
            }

        })
        viewModel.send(ProfileEvent.Load(username))
        viewModel.send(ProfileEvent.LoadLikePhoto(username))
        Log.d("onStartPhotoProfile", username)
    }

    private fun showLikedPhoto(response: PhotoResponse) {
        imageAdapter.submitList(response)
        if (response.isEmpty()) {
            Log.d("ProfileCollectionIf", "empty")
            noDataLabel.visibility = View.VISIBLE
            noDataLabel.text = "$username non ha ancora collection"
        }
        Log.d("ProfileCollectionScreen", "showCollections:$response")
    }

    private fun showProfile(response: Profile) {
        Log.d("ProfileScreen", "showProfile:${response.username}")
        profileImage = findViewById(R.id.profile_image)
        profileUsername = findViewById(R.id.profile_text_username)
        profileFullName = findViewById(R.id.profile_text_full_name)

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
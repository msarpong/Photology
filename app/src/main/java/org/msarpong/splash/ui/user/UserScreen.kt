package org.msarpong.splash.ui.user

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.koin.android.ext.android.inject
import org.msarpong.splash.R
import org.msarpong.splash.service.mapping.photos.PhotoResponse
import org.msarpong.splash.service.mapping.photos.PhotoResponseItem
import org.msarpong.splash.service.mapping.profile.Profile
import org.msarpong.splash.ui.collections.CollectionScreen
import org.msarpong.splash.ui.main.MainAdapter
import org.msarpong.splash.ui.main.MainScreen
import org.msarpong.splash.ui.main.UnsplashViewHolder
import org.msarpong.splash.ui.search.SearchPhotoScreen
import org.msarpong.splash.util.ACCESS_TOKEN
import org.msarpong.splash.util.sharedpreferences.KeyValueStorage

class UserScreen : AppCompatActivity() {

    private val viewModel: UserViewModel by inject()
    private val prefs: KeyValueStorage by inject()

    private lateinit var progressBar: ProgressBar

    private lateinit var homeBtn: ImageButton
    private lateinit var collectionBtn: ImageButton
    private lateinit var searchBtn: ImageButton
    private lateinit var profileBtn: ImageButton

    private lateinit var profileImage: ImageView
    private lateinit var profileUsername: TextView
    private lateinit var profileFullName: TextView
    private lateinit var profileBio: TextView

    private lateinit var profilePhotoBtn: Button
    private lateinit var profileLikeBtn: Button
    private lateinit var profileCollectionBtn: Button

    private lateinit var imageRV: RecyclerView
    private lateinit var imageAdapter: ListAdapter<PhotoResponseItem, UnsplashViewHolder>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_screen)
        initViews()
        setupViews()
    }

    private fun initViews() {
        progressBar = findViewById(R.id.progressBar)
        homeBtn = findViewById(R.id.home_btn)
        collectionBtn = findViewById(R.id.collection_btn)
        searchBtn = findViewById(R.id.search_btn)
        profileBtn = findViewById(R.id.profile_btn)
        profileImage = findViewById(R.id.profile_image)
        profileUsername = findViewById(R.id.profile_text_username)
        profileFullName = findViewById(R.id.profile_text_fullname)
        profileBio = findViewById(R.id.profile_text_bio)
        profilePhotoBtn = findViewById(R.id.profile_photo)
        profileLikeBtn = findViewById(R.id.profile_like)
        profileCollectionBtn = findViewById(R.id.profile_collection)
        profilePhotoBtn.typeface = Typeface.DEFAULT_BOLD
        profilePhotoBtn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16F)
    }

    private fun setupViews() {
        imageRV = findViewById(R.id.main_image)
        imageRV.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        imageAdapter = MainAdapter()
        imageRV.adapter = imageAdapter

        profileBtn.setColorFilter(ContextCompat.getColor(this, R.color.active_button))

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
    }

    override fun onStart() {
        super.onStart()

        viewModel.state.observe(this, Observer { state ->
            when (state) {
                is UserState.InProgress -> showProgress()
                is UserState.Error -> {
                    hideProgress()
                    showError(state.error)
                }
                is UserState.Success -> {
                    hideProgress()
                    viewModel.send(UserEvent.LoadProfile(state.user.username))
                    viewModel.send((UserEvent.LoadPhoto(state.user.username)))
                }
                is UserState.SuccessProfile -> {
                    hideProgress()
                    showProfile(state.profile)
                }
                is UserState.SuccessPhoto -> {
                    hideProgress()
                    showPhotos(state.photoList)
                }
            }
        })
        val token = prefs.getString(ACCESS_TOKEN)
        Log.d("showToken", "token: $token")

        viewModel.send(UserEvent.Load(token.toString()))
    }

    private fun showPhotos(photoList: PhotoResponse) {
        imageAdapter.submitList(photoList)
        Log.d("UserScreenR", "showPhotos:$photoList")
    }

    private fun showProfile(profile: Profile) {
//        Log.d("showProfile", "showError: ${profile.}")
        Glide
            .with(profileImage.context)
            .load(profile.profileImage.large)
            .fitCenter()
            .into(profileImage)

        profileUsername.text = profile.username
        profileFullName.text = profile.name
        profileBio.text = profile.bio
    }

    private fun showError(error: Throwable) {
        Log.d("UserScreen", "showError: $error")
    }

    private fun showProgress() {
        progressBar.visibility = View.VISIBLE
    }

    private fun hideProgress() {
        progressBar.visibility = View.GONE
    }


}
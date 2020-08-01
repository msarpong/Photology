package org.msarpong.splash.ui.profile

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
import org.msarpong.splash.R
import org.msarpong.splash.service.mapping.collection.Collection
import org.msarpong.splash.service.mapping.collection.CollectionItem
import org.msarpong.splash.service.mapping.profile.Profile
import org.msarpong.splash.ui.collections.CollectionAdapter
import org.msarpong.splash.ui.collections.CollectionScreen
import org.msarpong.splash.ui.collections.CollectionViewHolder
import org.msarpong.splash.ui.main.MainScreen
import org.msarpong.splash.ui.search.SearchPhotoScreen

private const val BUNDLE_ID: String = "BUNDLE_ID"

class ProfileCollectionScreen : AppCompatActivity() {

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
    private lateinit var profilePhotoBtn: Button
    private lateinit var profileLikeBtn: Button
    private lateinit var profileCollectionBtn: Button
    private lateinit var noDataLabel: TextView

    private lateinit var collectionsRV: RecyclerView
    private lateinit var collectionsAdapter: ListAdapter<CollectionItem, CollectionViewHolder>

    companion object {
        fun openCollectionProfile(startingActivity: Activity, detailId: String) {
            val intent =
                Intent(startingActivity, ProfileCollectionScreen::class.java).putExtra(
                    BUNDLE_ID,
                    detailId
                )
            startingActivity.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_collection_screen)
        initView()
        setupViews()

    }

    private fun initView() {
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
        profilePhotoBtn = findViewById(R.id.profile_photo)
        profileLikeBtn = findViewById(R.id.profile_like)
        profileCollectionBtn = findViewById(R.id.profile_collection)
        profileCollectionBtn.typeface = Typeface.DEFAULT_BOLD
        profileCollectionBtn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16F)
        noDataLabel = findViewById(R.id.no_data_text)

        collectionsRV = findViewById(R.id.collection_image)
        collectionsRV.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        collectionsAdapter = CollectionAdapter()
        collectionsRV.adapter = collectionsAdapter

    }

    private fun setupViews() {
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
            ProfilePhotoScreen.openPhotoProfile(this, username)
        }

        profilePhotoBtn.setOnClickListener {
            ProfilePhotoScreen.openPhotoProfile(this, username)
        }
        profileLikeBtn.setOnClickListener {
            ProfileLikeScreen.openLikeProfile(this, username)
        }
        profileCollectionBtn.setOnClickListener {
            openCollectionProfile(this, username)
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
                is ProfileState.SuccessCollection -> {
                    hideProgress()
                    showCollections(state.collection)
                }
            }

        })
        viewModel.send(ProfileEvent.Load(username))
        viewModel.send(ProfileEvent.LoadCollection(username))
        Log.d("onStartPhotoProfile", username)
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

    private fun showCollections(response: Collection) {
        collectionsAdapter.submitList(response)
        if (response.isEmpty()) {
            Log.d("ProfileCollectionIf", "empty")
            noDataLabel.visibility = View.VISIBLE
            noDataLabel.text = "$username non ha ancora collection"
        }
        Log.d("ProfileCollectionScreen", "showCollections:$response")
    }

    private fun showProgress() {
        progressBar.visibility = View.VISIBLE
    }

    private fun hideProgress() {
        progressBar.visibility = View.GONE
    }

    private fun showError(error: Throwable) {
        Log.d("ProfileCollectionScreen", "showError: $error")
    }

}
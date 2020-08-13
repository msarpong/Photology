package org.msarpong.photology.ui.following

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
import org.msarpong.photology.R
import org.msarpong.photology.service.mapping.following.FollowingResponse
import org.msarpong.photology.service.mapping.following.FollowingResponseItem
import org.msarpong.photology.ui.collections.CollectionScreen
import org.msarpong.photology.ui.main.MainScreen
import org.msarpong.photology.ui.search.SearchPhotoScreen
import org.msarpong.photology.ui.settings.SettingsScreen
import org.msarpong.photology.ui.user.UserScreen
import org.msarpong.photology.util.ACCESS_TOKEN
import org.msarpong.photology.util.sharedpreferences.KeyValueStorage


class FollowingScreen : AppCompatActivity() {

    private val viewModel: FollowingViewModel by inject()
    private val prefs: KeyValueStorage by inject()

    private lateinit var progressBar: ProgressBar
    private lateinit var username: String

    private lateinit var homeBtn: ImageButton
    private lateinit var collectionBtn: ImageButton
    private lateinit var searchBtn: ImageButton
    private lateinit var profileBtn: ImageButton
    private lateinit var editorialBtn: Button
    private lateinit var followingBtn: Button
    private lateinit var settingBtn: ImageButton

    private lateinit var followingRV: RecyclerView
    private lateinit var followingAdapter: ListAdapter<FollowingResponseItem, FollowingViewHolder>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.following_screen)
        initViews()
        setupViews()
        initRecycler()
    }

    private fun initRecycler() {
        followingRV = findViewById(R.id.main_image)
        followingRV.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        followingAdapter = FollowingAdapter()
        followingRV.adapter = followingAdapter
    }

    private fun initViews() {
        username = prefs.getString("username").toString()

        progressBar = findViewById(R.id.progressBar)
        settingBtn = findViewById(R.id.setting_btn)
        homeBtn = findViewById(R.id.home_btn)
        collectionBtn = findViewById(R.id.collection_btn)
        searchBtn = findViewById(R.id.search_btn)
        profileBtn = findViewById(R.id.profile_btn)
        editorialBtn = findViewById(R.id.editorial_btn)
        followingBtn = findViewById(R.id.following_btn)
        followingRV = findViewById(R.id.main_image)
    }

    private fun setupViews() {
        homeBtn.setColorFilter(ContextCompat.getColor(this, R.color.active_button))
        if (!prefs.getString(ACCESS_TOKEN).isNullOrEmpty()) {
            settingBtn.visibility = View.VISIBLE
        }
        editorialBtn.setCompoundDrawables(null, null, null, null)
        collectionBtn.setOnClickListener {
            startActivity(Intent(this, CollectionScreen::class.java))
        }
        searchBtn.setOnClickListener {
            startActivity(Intent(this, SearchPhotoScreen::class.java))
        }
        profileBtn.setOnClickListener {
            startActivity(Intent(this, UserScreen::class.java))
        }
        editorialBtn.setOnClickListener {
            startActivity(Intent(this, MainScreen::class.java))
        }
        settingBtn.setOnClickListener {
            startActivity(Intent(this, SettingsScreen::class.java))
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.state.observe(this, Observer { state ->
            when (state) {
                is FollowingState.InProgress -> showProgress()

                is FollowingState.SuccessUser -> {
                    hideProgress()
                    showUser(state.userList)
                }

                is FollowingState.SuccessPhoto -> {
                    hideProgress()
//                    showUser(state.userList)
                }
                is FollowingState.Error -> {
                    hideProgress()
                    showError(state.error)
                }
            }
        })
        viewModel.send(FollowingEvent.LoadUser(username))
        Log.d("onStartFollow", username)

    }

    private fun showUser(userList: FollowingResponse) {
        followingAdapter.submitList(userList)
        Log.d("FollowingScreen_user", "${userList}")
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
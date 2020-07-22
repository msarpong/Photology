package org.msarpong.splash.ui.collections

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ProgressBar
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.koin.android.ext.android.inject
import org.msarpong.splash.R
import org.msarpong.splash.service.collection.Collection
import org.msarpong.splash.service.collection.CollectionItem
import org.msarpong.splash.service.mapping.Unsplash
import org.msarpong.splash.service.mapping.UnsplashItem
import org.msarpong.splash.ui.main.*
import org.msarpong.splash.ui.search.SearchScreen
import org.msarpong.splash.ui.setting.SettingScreen

class CollectionScreen : AppCompatActivity() {

    private val viewModel: CollectionViewModel by inject()

    private lateinit var progressBar: ProgressBar

    private lateinit var homeBtn: ImageButton
    private lateinit var collectionBtn: ImageButton
    private lateinit var searchBtn: ImageButton
    private lateinit var profileBtn: ImageButton

    private lateinit var collectionsRV: RecyclerView
    private lateinit var collectionsAdapter: ListAdapter<CollectionItem, CollectionViewHolder>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.collection_screen)
        setupViews()
    }

    private fun setupViews() {
        progressBar = findViewById(R.id.progressBar)

        homeBtn = findViewById(R.id.home_btn)
        collectionBtn = findViewById(R.id.collection_btn)
        searchBtn = findViewById(R.id.search_btn)
        profileBtn = findViewById(R.id.profile_btn)

        collectionsRV = findViewById(R.id.collection_image)
        collectionsRV.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        collectionsAdapter = CollectionAdapter()
        collectionsRV.adapter = collectionsAdapter

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
    }

    override fun onStart() {
        super.onStart()
        viewModel.state.observe(this, Observer { state ->

            when (state) {
                is CollectionState.InProgress -> showProgress()
                is CollectionState.Error -> {
                    hideProgress()
                    showError(state.error)
                }
                is CollectionState.Success -> {
                    hideProgress()
                    showCollections(state.collection)
                }
            }
        })
        viewModel.send(CollectionEvent.Load)
    }

    private fun showCollections(response: Collection) {
        collectionsAdapter.submitList(response)
        Log.d("CollectionScreen", "showCollections:$response")
    }

    private fun showProgress() {
        progressBar.visibility = View.VISIBLE
    }

    private fun hideProgress() {
        progressBar.visibility = View.GONE
    }

    private fun showError(error: Throwable) {
        Log.d("CollectionScreen", "showError: $error")
    }
}

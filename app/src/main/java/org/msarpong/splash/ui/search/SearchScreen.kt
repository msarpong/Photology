package org.msarpong.splash.ui.search

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.koin.android.ext.android.inject
import org.msarpong.splash.R
import org.msarpong.splash.service.mapping.search.SearchResponse
import org.msarpong.splash.ui.collections.CollectionScreen
import org.msarpong.splash.ui.main.MainScreen
import org.msarpong.splash.ui.user.UserScreen
import org.msarpong.splash.util.hideKeyboard

class SearchScreen : AppCompatActivity() {

    private val viewModel: SearchViewModel by inject()

    private lateinit var progressBar: ProgressBar

    private lateinit var homeBtn: ImageButton
    private lateinit var collectionBtn: ImageButton
    private lateinit var searchBtn: ImageButton
    private lateinit var profileBtn: ImageButton
    private lateinit var searchTerm: EditText
    private lateinit var submitQuery: ImageButton

    private lateinit var imageRV: RecyclerView
    private lateinit var imageAdapter: ListAdapter<SearchResponse.Result, SearchViewHolder>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_screen)
        initViews()
        initRecyclerView()
        setupViews()
    }

    private fun initRecyclerView() {
        imageRV.layoutManager = GridLayoutManager(this, 3)
        imageAdapter = SearchAdapter()
        imageRV.adapter = imageAdapter
    }

    private fun initViews() {
        progressBar = findViewById(R.id.progressBar)
        homeBtn = findViewById(R.id.home_btn)
        collectionBtn = findViewById(R.id.collection_btn)
        searchBtn = findViewById(R.id.search_btn)
        profileBtn = findViewById(R.id.profile_btn)
        submitQuery = findViewById(R.id.search_submit_btn)
        searchTerm = findViewById(R.id.search_edit_text)
        imageRV = findViewById(R.id.recycler_search)
    }

    private fun setupViews() {

        searchBtn.setColorFilter(ContextCompat.getColor(this, R.color.active_button))

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
            startActivity(Intent(this, UserScreen::class.java))
        }

        submitQuery.setOnClickListener {
            val query = searchTerm.text.toString()
            viewModel.send(SearchEvent.Load(query))
            this.hideKeyboard()
        }
    }

    override fun onStart() {

        super.onStart()
        viewModel.state.observe(this, Observer { state ->
            when (state) {
                is SearchState.Error -> {
                    hideProgress()
                    showError(state.error)
                }
                is SearchState.Success -> {
                    hideProgress()
                    showResult(state.result)
                }
            }
        })
    }

    private fun showResult(response: SearchResponse) {
        imageAdapter.submitList(response.results)
        Log.d("SearchScreen", "showSearchScreen:$response")
    }

    private fun hideProgress() {
        progressBar.visibility = View.GONE
    }

    private fun showError(error: Throwable) {
        Log.d("SearchScreenError", "showError: $error")
    }
}

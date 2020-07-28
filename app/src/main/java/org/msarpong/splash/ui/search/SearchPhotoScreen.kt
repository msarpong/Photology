package org.msarpong.splash.ui.search

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.WindowManager
import android.widget.*
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
import org.msarpong.splash.util.SEARCH_TYPE_PHOTOS
import org.msarpong.splash.util.hideKeyboard

class SearchPhotoScreen : AppCompatActivity() {

    private val viewModel: SearchViewModel by inject()

    private lateinit var term: String

    private lateinit var progressBar: ProgressBar
    private lateinit var homeBtn: ImageButton
    private lateinit var collectionBtn: ImageButton
    private lateinit var searchBtn: ImageButton
    private lateinit var profileBtn: ImageButton
    private lateinit var searchTerm: EditText
    private lateinit var submitQuery: ImageButton
    private lateinit var totalResult: TextView
    private lateinit var searchBar: View
    private lateinit var searchPhoto: Button
    private lateinit var searchUser: Button
    private lateinit var searchCollection: Button

    private lateinit var imageRV: RecyclerView
    private lateinit var imageAdapter: ListAdapter<SearchResponse.Result, SearchViewHolder>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_screen)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
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
        totalResult = findViewById(R.id.search_result_nr)
        searchBar = findViewById(R.id.search_bar)
        searchTerm = findViewById(R.id.search_edit_text)
        searchPhoto = findViewById(R.id.search_bar_photo)
        searchUser = findViewById(R.id.search_bar_user)
        searchCollection = findViewById(R.id.search_bar_collection)

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
            startActivity(Intent(this, SearchPhotoScreen::class.java))
        }
        profileBtn.setOnClickListener {
            startActivity(Intent(this, UserScreen::class.java))
        }

        searchUser.setOnClickListener {
            startActivity(Intent(this, SearchUserScreen::class.java))
            SearchUserScreen.openSearchUser(this, term)
            Log.d("searchUser", "Query:$term")

        }

        searchCollection.setOnClickListener {
            startActivity(Intent(this, SearchCollectionScreen::class.java))
        }

        searchPhoto.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16F)
        searchPhoto.setTypeface(searchUser.typeface, Typeface.BOLD)

        submitQuery.setOnClickListener {
            val query = searchTerm.text.toString()
            term = searchTerm.text.toString()

            viewModel.send(SearchEvent.Load(SEARCH_TYPE_PHOTOS, query))
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
        val total = response.total.toString()
        totalResult.visibility = View.VISIBLE
        searchBar.visibility = View.VISIBLE
        totalResult.text = "$total result found for $term"
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

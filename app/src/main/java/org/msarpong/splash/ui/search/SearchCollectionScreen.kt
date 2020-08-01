package org.msarpong.splash.ui.search

import android.app.Activity
import android.content.Intent
import android.graphics.Color
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.koin.android.ext.android.inject
import org.msarpong.splash.R
import org.msarpong.splash.service.mapping.search.collections.SearchCollectionResponse
import org.msarpong.splash.ui.collections.CollectionScreen
import org.msarpong.splash.ui.main.MainScreen
import org.msarpong.splash.ui.profile.ProfilePhotoScreen
import org.msarpong.splash.util.hideKeyboard
import org.msarpong.splash.util.sharedpreferences.KeyValueStorage

private const val BUNDLE_QUERY: String = "BUNDLE_QUERY"

class SearchCollectionScreen : AppCompatActivity() {
    private val viewModel: SearchViewModel by inject()
    private val prefs: KeyValueStorage by inject()

    private lateinit var username: String
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
    private lateinit var collectionRV: RecyclerView
    private lateinit var collectionAdapter: ListAdapter<SearchCollectionResponse.Result, SearchCollectionViewHolder>

    companion object {
        fun openSearchCollection(startingActivity: Activity, queryTerm: String) {
            val intent =
                Intent(startingActivity, SearchCollectionScreen::class.java).putExtra(
                    BUNDLE_QUERY,
                    queryTerm
                )
            startingActivity.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_collection_screen)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        initViews()
        initRecyclerView()
        setupViews()
    }

    private fun initRecyclerView() {
        collectionRV.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        collectionAdapter = SearchCollectionAdapter()
        collectionRV.adapter = collectionAdapter
    }

    private fun initViews() {
        username = prefs.getString("username").toString()
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
        collectionRV = findViewById(R.id.search_collection_screen)
    }

    private fun setupViews() {
        if (intent.hasExtra(BUNDLE_QUERY)) {
            term = intent.getStringExtra(BUNDLE_QUERY)
            viewModel.send(SearchEvent.LoadUser(term))
            viewModel.send(SearchEvent.Load(term)).toString()
            viewModel.send(SearchEvent.LoadCollection(term))

            searchTerm.setText(term)
        } else {
            searchTerm.text.toString()
        }
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
            ProfilePhotoScreen.openPhotoProfile(this, username)
        }

        searchUser.setOnClickListener {
            SearchUserScreen.openSearchUser(this, term)
        }

        searchPhoto.setOnClickListener {
            SearchPhotoScreen.openSearchPhoto(this, term)
        }

        searchCollection.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16F)
        searchCollection.setTypeface(searchUser.typeface, Typeface.BOLD)

        submitQuery.setOnClickListener {
            val query = searchTerm.text.toString()
            if (query.isNotEmpty()) {
                term = searchTerm.text.toString()
                Log.d("submitQuery_Col", "Query:$query")

                viewModel.send(SearchEvent.LoadCollection(query))
                this.hideKeyboard()
            } else {
                searchTerm.setHintTextColor(Color.RED)
            }
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
                is SearchState.SuccessCollection -> {
                    hideProgress()
                    showResult(state.result)
                }
            }
        })
    }

    private fun showResult(response: SearchCollectionResponse) {
        val total = response.total.toString()
        totalResult.visibility = View.VISIBLE
        searchBar.visibility = View.VISIBLE
        totalResult.text = "$total result found for $term"
        collectionAdapter.submitList(response.results)
        Log.d("SearchColScreen", "showSearchScreen:$response")
    }

    private fun hideProgress() {
        progressBar.visibility = View.GONE
    }

    private fun showError(error: Throwable) {
        Log.d("SearchScreenError", "showError: $error")
    }
}
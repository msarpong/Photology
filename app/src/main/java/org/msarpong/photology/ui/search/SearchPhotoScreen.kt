package org.msarpong.photology.ui.search

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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.koin.android.ext.android.inject
import org.msarpong.photology.R
import org.msarpong.photology.service.mapping.search.SearchResponse
import org.msarpong.photology.ui.collections.CollectionScreen
import org.msarpong.photology.ui.main.MainScreen
import org.msarpong.photology.ui.user.UserScreen
import org.msarpong.photology.util.hideKeyboard

private const val BUNDLE_QUERY: String = "BUNDLE_QUERY"

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

    companion object {
        fun openSearchPhoto(startingActivity: Activity, queryTerm: String) {
            val intent =
                Intent(startingActivity, SearchPhotoScreen::class.java).putExtra(
                    BUNDLE_QUERY,
                    queryTerm
                )
            startingActivity.startActivity(intent)
        }
    }

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
        imageRV = findViewById(R.id.recycler_search_user)

        if (intent.hasExtra(BUNDLE_QUERY)) {
            term = intent.getStringExtra(BUNDLE_QUERY)
            viewModel.send(SearchEvent.LoadUser(term))
            viewModel.send(SearchEvent.Load(term)).toString()
            viewModel.send(SearchEvent.LoadCollection(term))
            searchTerm.setText(term)
        } else {
            searchTerm.text.toString()
        }
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
            SearchUserScreen.openSearchUser(this, term)
        }

        searchCollection.setOnClickListener {
            SearchCollectionScreen.openSearchCollection(this, term)
        }

        searchPhoto.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16F)
        searchPhoto.setTypeface(searchUser.typeface, Typeface.BOLD)

        submitQuery.setOnClickListener {
            val query = searchTerm.text.toString()
            if(query.isNotEmpty()){
                term = searchTerm.text.toString()
                Log.d("submitQuery_Photo", "Query:$query")

                viewModel.send(SearchEvent.Load(query))
                this.hideKeyboard()
            }else{
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
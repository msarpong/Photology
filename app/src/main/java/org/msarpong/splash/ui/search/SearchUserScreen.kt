package org.msarpong.splash.ui.search

import android.app.Activity
import android.content.Intent
import android.graphics.Color
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
import org.koin.android.ext.android.inject
import org.msarpong.splash.R
import org.msarpong.splash.service.mapping.search.users.SearchUserResponse
import org.msarpong.splash.ui.collections.CollectionScreen
import org.msarpong.splash.ui.main.MainScreen
import org.msarpong.splash.ui.user.UserScreen
import org.msarpong.splash.util.hideKeyboard

private const val BUNDLE_QUERY: String = "BUNDLE_QUERY"

class SearchUserScreen : AppCompatActivity() {

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

    private lateinit var usersRV: RecyclerView
    private lateinit var usersAdapter: ListAdapter<SearchUserResponse.Result, SearchUserViewHolder>

    companion object {
        fun openSearchUser(startingActivity: Activity, queryTerm: String) {
            val intent =
                Intent(startingActivity, SearchUserScreen::class.java).putExtra(
                    BUNDLE_QUERY,
                    queryTerm
                )
            startingActivity.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_user_screen)
        initViews()
        initRecyclerView()
        setupViews()
    }

    private fun initRecyclerView() {
        usersRV.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        usersAdapter = SearchUserAdapter()
        usersRV.adapter = usersAdapter
    }

    private fun initViews() {
        term = intent.getStringExtra(BUNDLE_QUERY)

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

        usersRV = findViewById(R.id.recycler_search_user)
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

        searchUser.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16F)
        searchUser.setTypeface(searchUser.typeface, Typeface.BOLD)

        searchPhoto.setOnClickListener {
            SearchPhotoScreen.openSearchPhoto(this, term)
        }

        searchCollection.setOnClickListener {
            SearchCollectionScreen.openSearchCollection(this, term)
        }

        searchTerm.setText(term)

        submitQuery.setOnClickListener {
            val query = searchTerm.text.toString()
            if(query.isNotEmpty()){
                term = searchTerm.text.toString()
                Log.d("submitQuery_User", "Query:$query")

                viewModel.send(SearchEvent.LoadUser(query))
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
                is SearchState.SuccessUser -> {
                    hideProgress()
                    showResult(state.result)
                }
            }
        })
        viewModel.send(SearchEvent.LoadUser(term))
        Log.d("onStart", "Query:$term")

    }

    private fun showResult(response: SearchUserResponse) {
        val total = response.total.toString()
        totalResult.visibility = View.VISIBLE
        searchBar.visibility = View.VISIBLE
        totalResult.text = "$total result found for $term"
        usersAdapter.submitList(response.results)
        Log.d("SearchUser", "showSearchUser:$response")
    }

    private fun hideProgress() {
        progressBar.visibility = View.GONE
    }

    private fun showError(error: Throwable) {
        Log.d("SearchScreenError", "showError: $error")
    }

}
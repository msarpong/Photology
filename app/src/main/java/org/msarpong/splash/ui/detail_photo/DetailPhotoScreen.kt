package org.msarpong.splash.ui.detail_photo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import org.koin.android.ext.android.inject
import org.msarpong.splash.R
import org.msarpong.splash.ui.collections.CollectionScreen
import org.msarpong.splash.ui.main.MainScreen
import org.msarpong.splash.ui.search.SearchScreen
import org.msarpong.splash.ui.setting.SettingScreen

class DetailPhotoScreen : AppCompatActivity() {

    private val viewModel: DetailPhotoViewModel by inject()

    private lateinit var progressBar: ProgressBar
    private lateinit var collectionBtn: Button
    private lateinit var homeBtn: Button
    private lateinit var searchBtn: Button
    private lateinit var profileBtn: Button

    companion object {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_photo_screen)
        setupViews()

    }

    private fun setupViews() {
        progressBar = findViewById(R.id.progressBar)

        homeBtn = findViewById(R.id.home_btn)
        collectionBtn = findViewById(R.id.collection_btn)
        searchBtn = findViewById(R.id.search_btn)
        profileBtn = findViewById(R.id.profile_btn)

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


}
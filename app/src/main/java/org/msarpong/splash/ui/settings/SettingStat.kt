package org.msarpong.splash.ui.settings

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import org.koin.android.ext.android.inject
import org.msarpong.splash.R
import org.msarpong.splash.ui.collections.CollectionScreen
import org.msarpong.splash.ui.main.MainScreen
import org.msarpong.splash.ui.search.SearchPhotoScreen
import org.msarpong.splash.ui.user.UserScreen
import org.msarpong.splash.util.ACCESS_TOKEN
import org.msarpong.splash.util.sharedpreferences.KeyValueStorage

class SettingStat : AppCompatActivity() {

    private val prefs: KeyValueStorage by inject()
    private val viewModel: SettingsViewModel by inject()

    private lateinit var homeBtn: ImageButton
    private lateinit var collectionBtn: ImageButton
    private lateinit var searchBtn: ImageButton
    private lateinit var profileBtn: ImageButton
    private lateinit var settingBtn: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.setting_stat)

        initViews()
        setupViews()
    }

    private fun initViews() {
        settingBtn = findViewById(R.id.setting_btn)
        homeBtn = findViewById(R.id.home_btn)
        collectionBtn = findViewById(R.id.collection_btn)
        searchBtn = findViewById(R.id.search_btn)
        profileBtn = findViewById(R.id.profile_btn)
    }

    private fun setupViews() {
        if (!prefs.getString(ACCESS_TOKEN).isNullOrEmpty()) {
            settingBtn.visibility = View.VISIBLE
        }

        settingBtn.setColorFilter(ContextCompat.getColor(this, R.color.active_button))

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
        settingBtn.setOnClickListener {
            startActivity(Intent(this, SettingsScreen::class.java))
        }


    }
}
package org.msarpong.photology.ui.settings

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import org.koin.android.ext.android.inject
import org.msarpong.photology.R
import org.msarpong.photology.ui.collections.CollectionScreen
import org.msarpong.photology.ui.main.MainScreen
import org.msarpong.photology.ui.search.SearchPhotoScreen
import org.msarpong.photology.ui.user.UserScreen
import org.msarpong.photology.util.ACCESS_TOKEN
import org.msarpong.photology.util.LANGUAGE
import org.msarpong.photology.util.sharedpreferences.KeyValueStorage

class SettingLanguage : AppCompatActivity() {

    private val prefs: KeyValueStorage by inject()
    private val viewModel: SettingsViewModel by inject()

    private lateinit var homeBtn: ImageButton
    private lateinit var collectionBtn: ImageButton
    private lateinit var searchBtn: ImageButton
    private lateinit var profileBtn: ImageButton
    private lateinit var settingBtn: ImageButton
    private lateinit var editSaveBtn: Button
    private var radioGroupLanguage: RadioGroup? = null
    private lateinit var radioBtnLanguage: RadioButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.setting_language)
        initViews()
        setupViews()

    }

    private fun initViews() {
        settingBtn = findViewById(R.id.setting_btn)
        homeBtn = findViewById(R.id.home_btn)
        collectionBtn = findViewById(R.id.collection_btn)
        searchBtn = findViewById(R.id.search_btn)
        profileBtn = findViewById(R.id.profile_btn)
        editSaveBtn = findViewById(R.id.edit_save_btn)
        radioGroupLanguage = findViewById(R.id.radio_group_language)

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
        editSaveBtn.setOnClickListener {
            val intSelectButton: Int = radioGroupLanguage!!.checkedRadioButtonId
            radioBtnLanguage = findViewById(intSelectButton)
            prefs.putString(LANGUAGE, radioBtnLanguage.text as String?)
            Log.d("EditSaveBtn", prefs.getString(LANGUAGE))
            finish()
            Toast.makeText(this, "Language changed", Toast.LENGTH_LONG).show()
        }
    }
}
package org.msarpong.splash.ui.settings

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import org.koin.android.ext.android.inject
import org.msarpong.splash.R
import org.msarpong.splash.service.mapping.auth.user.UserResponse
import org.msarpong.splash.ui.collections.CollectionScreen
import org.msarpong.splash.ui.main.MainScreen
import org.msarpong.splash.ui.search.SearchPhotoScreen
import org.msarpong.splash.ui.user.UserScreen
import org.msarpong.splash.util.ACCESS_TOKEN
import org.msarpong.splash.util.sharedpreferences.KeyValueStorage

class SettingProfile : AppCompatActivity() {

    private val prefs: KeyValueStorage by inject()
    private val viewModel: SettingsViewModel by inject()

    private lateinit var saveData: EditUser

    private lateinit var editUsername: EditText
    private lateinit var editFirstName: EditText
    private lateinit var editLastName: EditText
    private lateinit var editEmail: EditText
    private lateinit var editBio: EditText
    private lateinit var editInstagram: EditText
    private lateinit var editSaveBtn: Button

    private lateinit var username: String
    private lateinit var firstName: String
    private lateinit var lastName: String
    private lateinit var eMail: String
    private lateinit var bio: String
    private lateinit var instagram: String

    private lateinit var homeBtn: ImageButton
    private lateinit var collectionBtn: ImageButton
    private lateinit var searchBtn: ImageButton
    private lateinit var profileBtn: ImageButton
    private lateinit var settingBtn: ImageButton

    private lateinit var authToken: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.setting_profile)
        initViews()
        setupViews()
    }

    private fun initViews() {
        settingBtn = findViewById(R.id.setting_btn)
        homeBtn = findViewById(R.id.home_btn)
        collectionBtn = findViewById(R.id.collection_btn)
        searchBtn = findViewById(R.id.search_btn)
        profileBtn = findViewById(R.id.profile_btn)

        editUsername = findViewById(R.id.edit_username)
        editFirstName = findViewById(R.id.edit_first_name)
        editLastName = findViewById(R.id.edit_last_name)
        editEmail = findViewById(R.id.edit_email)
        editBio = findViewById(R.id.edit_bio)
        editInstagram = findViewById(R.id.edit_instagram)
        editSaveBtn = findViewById(R.id.edit_save_btn)

        authToken = prefs.getString(ACCESS_TOKEN).toString()
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

        editSaveBtn.setOnClickListener {
            username = editUsername.text.toString()
            firstName = editFirstName.text.toString()
            lastName = editLastName.text.toString()
            eMail = editEmail.text.toString()
            bio = editBio.text.toString()
            instagram = editInstagram.text.toString()

            saveData = EditUser(username, firstName, lastName, eMail, bio, instagram)
            Log.d("SettingsProfile", "Response: $saveData")
            Log.d("SettingsProfileActivity", "editSaveBtn: ${saveData.bio}")

            viewModel.send(SettingEvent.Edit(authToken, saveData))
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.state.observe(this, Observer { state ->
            when (state) {
                is SettingState.Error -> showError(state.error)
                is SettingState.Success -> showResult(state.user)
                is SettingState.SuccessEdit -> successEdit(state.user)
            }
        })
        viewModel.send(SettingEvent.Load(authToken))
    }

    private fun successEdit(user: UserResponse) {
        Log.d("SettingsProfileActivity", "successEdit: $user")
        Toast.makeText(this, "Profile update!", Toast.LENGTH_LONG).show()
    }

    private fun showResult(user: UserResponse) {
        Log.d("SettingsProfileActivity", "Response: $user")
        editUsername.setText(user.username).toString()
        editFirstName.setText(user.firstName)
        editLastName.setText(user.lastName)
        editEmail.setText(user.email)
        editBio.setText(user.bio)
        editInstagram.setText(user.instagramUsername)
    }

    private fun showError(error: Throwable) {
        Log.d("SettingsProfile", "showError: $error")
    }

}

package org.msarpong.splash.ui.detail_photo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import org.koin.android.ext.android.inject
import org.msarpong.splash.R
import org.msarpong.splash.service.mapping.detail_photo.DetailPhotoResponse
import org.msarpong.splash.ui.collections.CollectionScreen
import org.msarpong.splash.ui.main.MainScreen
import org.msarpong.splash.ui.search.SearchScreen
import org.msarpong.splash.ui.setting.SettingScreen

private const val BUNDLE_ID: String = "BUNDLE_ID"

class DetailPhotoScreen : AppCompatActivity() {

    private val viewModel: DetailPhotoViewModel by inject()

    private lateinit var progressBar: ProgressBar

    private lateinit var detailImage: ImageView
    private lateinit var detailId: String
    private lateinit var detailUserImage: ImageView
    private lateinit var detailUser: TextView
    private lateinit var detailUserName: TextView

    private lateinit var homeBtn: ImageButton
    private lateinit var collectionBtn: ImageButton
    private lateinit var searchBtn: ImageButton
    private lateinit var profileBtn: ImageButton

    private lateinit var detailInfo: ImageButton
    private lateinit var detailInfoView: View


    private lateinit var infoDate: TextView
    private lateinit var infoView: TextView
    private lateinit var infoDownload: TextView
    private lateinit var infoCamera: TextView
    private lateinit var infoCameraModel: TextView
    private lateinit var infoExposureTime: TextView
    private lateinit var infoAperture: TextView
    private lateinit var infoFocalLength: TextView
    private lateinit var infoIso: TextView


    companion object {
        fun openDetailPhoto(startingActivity: Activity, detailId: String) {
            val intent = Intent(startingActivity, DetailPhotoScreen::class.java)
                .putExtra(BUNDLE_ID, detailId)
            Log.d("openDetailPicture", detailId)
            startingActivity.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_photo_screen)
        setupViews()

    }

    private fun setupViews() {
        detailId = intent.getStringExtra(BUNDLE_ID)

        progressBar = findViewById(R.id.progressBar)

        homeBtn = findViewById(R.id.home_btn)
        collectionBtn = findViewById(R.id.collection_btn)
        searchBtn = findViewById(R.id.search_btn)
        profileBtn = findViewById(R.id.profile_btn)
        detailImage = findViewById(R.id.detail_image)
        detailUserImage = findViewById(R.id.detail_image_user)
        detailUser = findViewById(R.id.detail_text_name)
        detailUserName = findViewById(R.id.detail_text_username)
        detailInfo = findViewById(R.id.detail_info_button)
        detailInfoView = findViewById(R.id.view_detail_info)

        infoDate = findViewById(R.id.info_date)
        infoView = findViewById(R.id.info_view)
        infoDownload = findViewById(R.id.info_download)
        infoCamera = findViewById(R.id.info_exif_camera)
        infoCameraModel = findViewById(R.id.info_exif_camera_model)
        infoExposureTime = findViewById(R.id.info_exif_exposure_time)
        infoAperture = findViewById(R.id.info_exif_aperture)
        infoFocalLength = findViewById(R.id.info_exif_focal_length)
        infoIso = findViewById(R.id.info_exif_iso)

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
        detailInfo.setOnClickListener {
            if (detailInfoView.visibility == View.VISIBLE) {
                detailInfoView.visibility = View.GONE
            } else {
                detailInfoView.visibility = View.VISIBLE
            }
        }

        Log.d("setupViews", "detailId: $detailId")
    }

    override fun onStart() {
        super.onStart()
        viewModel.state.observe(this, Observer { state ->
            when (state) {
                is DetailState.InProgress -> showProgress()
                is DetailState.Error -> {
                    hideProgress()
                    showError(state.error)
                }
                is DetailState.Success -> {
                    hideProgress()
                    showPhoto(state.pictureDetail)
                }
            }
        })
        viewModel.send(DetailEvent.Load(detailId))
        Log.d("onStart", detailId)
    }

    private fun showPhoto(response: DetailPhotoResponse) {
        Glide
            .with(detailImage.context)
            .load(response.urls.small)
            .fitCenter()
            .into(detailImage)

        Glide
            .with(detailUser.context)
            .load(response.user.profileImage.medium)
            .fitCenter()
            .into(detailUserImage)

        detailUser.text = response.user.name
        detailUserName.text = response.user.username

        infoDate.text = "Published on "+response.createdAt
        infoView.text = response.views.toString()
        infoDownload.text = response.downloads.toString()
        infoCamera.text = response.exif.make
        infoCameraModel.text = response.exif.model
        infoExposureTime.text = response.exif.exposureTime
        infoAperture.text = response.exif.aperture
        infoFocalLength.text = response.exif.focalLength
        infoIso.text = response.exif.iso.toString()

        Log.d("DetailPhotoScreen", "showDetailPhoto:$response")
    }

    private fun showProgress() {
        progressBar.visibility = View.VISIBLE
    }

    private fun hideProgress() {
        progressBar.visibility = View.GONE
    }

    private fun showError(error: Throwable) {
        Log.d("MainActivity", "showError: $error")
    }
}
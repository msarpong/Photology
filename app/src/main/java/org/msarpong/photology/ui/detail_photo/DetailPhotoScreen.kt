package org.msarpong.photology.ui.detail_photo

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import org.koin.android.ext.android.inject
import org.msarpong.photology.R
import org.msarpong.photology.service.mapping.detail_photo.DetailPhotoResponse
import org.msarpong.photology.service.mapping.like.LikePhotoResponse
import org.msarpong.photology.ui.collections.CollectionScreen
import org.msarpong.photology.ui.main.MainScreen
import org.msarpong.photology.ui.profile.ProfilePhotoScreen
import org.msarpong.photology.ui.search.SearchPhotoScreen
import org.msarpong.photology.ui.user.UserScreen
import org.msarpong.photology.util.ACCESS_TOKEN
import org.msarpong.photology.util.DownloadPhoto
import org.msarpong.photology.util.sharedpreferences.KeyValueStorage

private const val BUNDLE_ID: String = "BUNDLE_ID"

class DetailPhotoScreen : AppCompatActivity() {

    private val viewModel: DetailPhotoViewModel by inject()
    private val prefs: KeyValueStorage by inject()

    private lateinit var progressBar: ProgressBar

    private lateinit var authToken: String
    private lateinit var detailId: String

    private lateinit var detailImage: ImageView
    private lateinit var detailUserImage: ImageView
    private lateinit var detailUser: TextView
    private lateinit var detailUserName: TextView
    private lateinit var homeBtn: ImageButton
    private lateinit var collectionBtn: ImageButton
    private lateinit var searchBtn: ImageButton
    private lateinit var profileBtn: ImageButton
    private lateinit var detailInfo: ImageButton
    private lateinit var detailShareBtn: ImageButton
    private lateinit var detailDownloadBtn: ImageButton
    private lateinit var detailLikeBtn: ToggleButton
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
    private lateinit var downloadManager: DownloadPhoto

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
        authToken = if (prefs.getString(ACCESS_TOKEN).isNullOrEmpty()) {
            "null"
        } else {
            prefs.getString(ACCESS_TOKEN).toString()
        }
        initView()
        setupViews()

    }

    private fun initView() {
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
        detailShareBtn = findViewById(R.id.detail_share_btn)
        detailDownloadBtn = findViewById(R.id.detail_download_btn)
        detailLikeBtn = findViewById(R.id.detail_like_btn)
        infoDate = findViewById(R.id.info_date)
        infoView = findViewById(R.id.info_view)
        infoDownload = findViewById(R.id.info_download)
        infoCamera = findViewById(R.id.info_exif_camera)
        infoCameraModel = findViewById(R.id.info_exif_camera_model)
        infoExposureTime = findViewById(R.id.info_exif_exposure_time)
        infoAperture = findViewById(R.id.info_exif_aperture)
        infoFocalLength = findViewById(R.id.info_exif_focal_length)
        infoIso = findViewById(R.id.info_exif_iso)
    }

    private fun setupViews() {

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
        detailInfo.setOnClickListener {
            if (detailInfoView.visibility == View.VISIBLE) {
                detailInfoView.visibility = View.GONE
            } else {
                detailInfoView.visibility = View.VISIBLE
            }
        }

        detailShareBtn.setOnClickListener {
            Toast.makeText(this, "detailDownloadBtn", Toast.LENGTH_LONG).show()
        }

        detailLikeBtn.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                viewModel.send(DetailEvent.LikePhoto(authToken, detailId))
            } else {
                viewModel.send(DetailEvent.UnLikePhoto(authToken, detailId))
            }
        }
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
                    showPhotos(state.pictureDetail)
                }
                is DetailState.SuccessLike -> {
                    setLike(state.likeResponse)
                }
                is DetailState.SuccessUnLike -> {
                    deleteLike(state.likeResponse)
                }
            }
        })
        viewModel.send(DetailEvent.Load(authToken, detailId))
        Log.d("getKey_onStart", "authToken: $authToken - detailId: $detailId")
    }

    private fun setLike(likeResponse: LikePhotoResponse) {
        Log.d("DetailPhotoScreen", "setLike:$likeResponse")
    }

    private fun deleteLike(likeResponse: LikePhotoResponse) {
        Log.d("DetailPhotoScreen", "deleteLike:$likeResponse")
    }

    private fun showPhotos(response: DetailPhotoResponse) {
        Glide
            .with(detailImage.context)
            .load(response.urls.regular)
            .fitCenter()
            .into(detailImage)

        Glide
            .with(detailUser.context)
            .load(response.user.profileImage.medium)
            .fitCenter()
            .into(detailUserImage)

        if (response.likedByUser) {
            detailLikeBtn.isChecked = true
        }

        detailUser.text = response.user.name
        detailUserName.text = response.user.username

        infoDate.text = "Published on " + response.createdAt
        infoView.text = response.views.toString()
        infoDownload.text = response.downloads.toString()
        infoCamera.text = response.exif.make
        infoCameraModel.text = response.exif.model
        infoExposureTime.text = response.exif.exposureTime
        infoAperture.text = response.exif.aperture
        infoFocalLength.text = response.exif.focalLength
        infoIso.text = response.exif.iso.toString()

        buttonListener(response)

        Log.d("DetailPhotoScreen", "showDetailPhoto:$response")
    }

    private fun buttonListener(response: DetailPhotoResponse) {

        downloadManager = DownloadPhoto(response.urls.small)
        detailUser.setOnClickListener {
            ProfilePhotoScreen.openPhotoProfile(this, response.user.username)
        }

        detailUserImage.setOnClickListener {
            ProfilePhotoScreen.openPhotoProfile(this, response.user.username)
        }

        detailDownloadBtn.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                downloadManager.askPermissions(this)
            } else {
                downloadManager.downloadImage(this, response.urls.regular)
            }
        }
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

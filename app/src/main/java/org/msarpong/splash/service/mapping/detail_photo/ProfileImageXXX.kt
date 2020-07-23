package org.msarpong.splash.service.mapping.detail_photo


import com.google.gson.annotations.SerializedName

data class ProfileImageXXX(
    @SerializedName("large")
    val large: String,
    @SerializedName("medium")
    val medium: String,
    @SerializedName("small")
    val small: String
)
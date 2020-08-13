package org.msarpong.photology.service.mapping.detail_photo


import com.google.gson.annotations.SerializedName

data class ProfileImage(
    @SerializedName("large")
    val large: String,
    @SerializedName("medium")
    val medium: String,
    @SerializedName("small")
    val small: String
)
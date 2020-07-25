package org.msarpong.splash.service.mapping.collection

import com.google.gson.annotations.SerializedName

data class ProfileImageX(
    @SerializedName("large")
    val large: String,
    @SerializedName("medium")
    val medium: String,
    @SerializedName("small")
    val small: String
)

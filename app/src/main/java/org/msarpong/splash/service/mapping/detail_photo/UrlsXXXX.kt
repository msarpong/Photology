package org.msarpong.splash.service.mapping.detail_photo


import com.google.gson.annotations.SerializedName

data class UrlsXXXX(
    @SerializedName("full")
    val full: String,
    @SerializedName("raw")
    val raw: String,
    @SerializedName("regular")
    val regular: String,
    @SerializedName("small")
    val small: String,
    @SerializedName("thumb")
    val thumb: String
)
package org.msarpong.photology.service.mapping.detail_photo


import com.google.gson.annotations.SerializedName

data class LinksXXXXXXX(
    @SerializedName("download")
    val download: String,
    @SerializedName("download_location")
    val downloadLocation: String,
    @SerializedName("html")
    val html: String,
    @SerializedName("self")
    val self: String
)
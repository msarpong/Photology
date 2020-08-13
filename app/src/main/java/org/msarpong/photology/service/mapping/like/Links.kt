package org.msarpong.photology.service.mapping.like


import com.google.gson.annotations.SerializedName

data class Links(
    @SerializedName("download")
    val download: String,
    @SerializedName("html")
    val html: String,
    @SerializedName("self")
    val self: String
)
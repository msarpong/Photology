package org.msarpong.splash.service.mapping.collection


import com.google.gson.annotations.SerializedName

data class LinksXX(
    @SerializedName("html")
    val html: String,
    @SerializedName("photos")
    val photos: String,
    @SerializedName("related")
    val related: String,
    @SerializedName("self")
    val self: String
)
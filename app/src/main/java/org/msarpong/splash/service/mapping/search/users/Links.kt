package org.msarpong.splash.service.mapping.search.users


import com.google.gson.annotations.SerializedName

data class Links(
    @SerializedName("followers")
    val followers: String,
    @SerializedName("following")
    val following: String,
    @SerializedName("html")
    val html: String,
    @SerializedName("likes")
    val likes: String,
    @SerializedName("photos")
    val photos: String,
    @SerializedName("portfolio")
    val portfolio: String,
    @SerializedName("self")
    val self: String
)
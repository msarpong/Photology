package org.msarpong.splash.service.mapping.like


import com.google.gson.annotations.SerializedName

data class Photo(
    @SerializedName("color")
    val color: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("height")
    val height: Int,
    @SerializedName("id")
    val id: String,
    @SerializedName("liked_by_user")
    val likedByUser: Boolean,
    @SerializedName("likes")
    val likes: Int,
    @SerializedName("links")
    val links: Links,
    @SerializedName("urls")
    val urls: Urls,
    @SerializedName("width")
    val width: Int
)
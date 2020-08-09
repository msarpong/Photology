package org.msarpong.splash.service.mapping.stats


import com.google.gson.annotations.SerializedName

data class StatsResponse(
    @SerializedName("downloads")
    val downloads: Downloads,
    @SerializedName("id")
    val id: String,
    @SerializedName("likes")
    val likes: Likes,
    @SerializedName("username")
    val username: String,
    @SerializedName("views")
    val views: Views
)
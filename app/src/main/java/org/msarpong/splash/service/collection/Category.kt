package org.msarpong.splash.service.collection


import com.google.gson.annotations.SerializedName

data class Category(
    @SerializedName("pretty_slug")
    val prettySlug: String,
    @SerializedName("slug")
    val slug: String
)
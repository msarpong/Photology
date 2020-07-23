package org.msarpong.splash.service.mapping.detail_photo


import com.google.gson.annotations.SerializedName

data class CategoryX(
    @SerializedName("pretty_slug")
    val prettySlug: String,
    @SerializedName("slug")
    val slug: String
)
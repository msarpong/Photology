package org.msarpong.photology.service.mapping.profile


import com.google.gson.annotations.SerializedName

data class CategoryX(
    @SerializedName("pretty_slug")
    val prettySlug: String,
    @SerializedName("slug")
    val slug: String
)
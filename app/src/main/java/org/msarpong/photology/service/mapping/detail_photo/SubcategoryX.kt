package org.msarpong.photology.service.mapping.detail_photo


import com.google.gson.annotations.SerializedName

data class SubcategoryX(
    @SerializedName("pretty_slug")
    val prettySlug: String,
    @SerializedName("slug")
    val slug: String
)
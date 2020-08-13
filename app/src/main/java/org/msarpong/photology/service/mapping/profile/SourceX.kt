package org.msarpong.photology.service.mapping.profile


import com.google.gson.annotations.SerializedName

data class SourceX(
    @SerializedName("ancestry")
    val ancestry: AncestryX,
    @SerializedName("cover_photo")
    val coverPhoto: CoverPhotoX,
    @SerializedName("description")
    val description: String,
    @SerializedName("meta_description")
    val metaDescription: String,
    @SerializedName("meta_title")
    val metaTitle: String,
    @SerializedName("subtitle")
    val subtitle: String,
    @SerializedName("title")
    val title: String
)
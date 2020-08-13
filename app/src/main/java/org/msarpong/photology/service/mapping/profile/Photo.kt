package org.msarpong.photology.service.mapping.profile


import com.google.gson.annotations.SerializedName

data class Photo(
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("urls")
    val urls: Urls
)
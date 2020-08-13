package org.msarpong.photology.service.mapping.detail_photo


import com.google.gson.annotations.SerializedName

data class Tag(
    @SerializedName("source")
    val source: Source,
    @SerializedName("title")
    val title: String,
    @SerializedName("type")
    val type: String
)
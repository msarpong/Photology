package org.msarpong.splash.service.mapping.profile


import com.google.gson.annotations.SerializedName

data class Aggregated(
    @SerializedName("source")
    val source: Source,
    @SerializedName("title")
    val title: String,
    @SerializedName("type")
    val type: String
)
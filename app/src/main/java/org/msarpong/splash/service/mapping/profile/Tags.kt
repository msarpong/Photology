package org.msarpong.splash.service.mapping.profile


import com.google.gson.annotations.SerializedName

data class Tags(
    @SerializedName("aggregated")
    val aggregated: List<Aggregated>,
    @SerializedName("custom")
    val custom: List<Custom>
)
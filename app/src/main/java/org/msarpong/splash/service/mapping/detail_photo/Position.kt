package org.msarpong.splash.service.mapping.detail_photo


import com.google.gson.annotations.SerializedName

data class Position(
    @SerializedName("latitude")
    val latitude: Any,
    @SerializedName("longitude")
    val longitude: Any
)
package org.msarpong.splash.service.mapping.detail_photo


import com.google.gson.annotations.SerializedName

data class Location(
    @SerializedName("city")
    val city: Any,
    @SerializedName("country")
    val country: Any,
    @SerializedName("name")
    val name: Any,
    @SerializedName("position")
    val position: Position,
    @SerializedName("title")
    val title: Any
)
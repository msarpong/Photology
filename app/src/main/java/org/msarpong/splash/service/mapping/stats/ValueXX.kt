package org.msarpong.splash.service.mapping.stats


import com.google.gson.annotations.SerializedName

data class ValueXX(
    @SerializedName("date")
    val date: String,
    @SerializedName("value")
    val value: Int
)
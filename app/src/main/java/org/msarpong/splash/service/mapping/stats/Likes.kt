package org.msarpong.splash.service.mapping.stats


import com.google.gson.annotations.SerializedName

data class Likes(
    @SerializedName("historical")
    val historical: HistoricalX,
    @SerializedName("total")
    val total: Int
)
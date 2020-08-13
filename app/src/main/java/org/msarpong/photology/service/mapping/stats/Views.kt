package org.msarpong.photology.service.mapping.stats


import com.google.gson.annotations.SerializedName

data class Views(
    @SerializedName("historical")
    val historical: HistoricalXX,
    @SerializedName("total")
    val total: Int
)
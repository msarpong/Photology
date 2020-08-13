package org.msarpong.photology.service.mapping.stats


import com.google.gson.annotations.SerializedName

data class HistoricalX(
    @SerializedName("average")
    val average: Int,
    @SerializedName("change")
    val change: Int,
    @SerializedName("quantity")
    val quantity: Int,
    @SerializedName("resolution")
    val resolution: String,
    @SerializedName("values")
    val values: List<ValueX>
)
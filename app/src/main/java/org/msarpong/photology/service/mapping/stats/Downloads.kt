package org.msarpong.photology.service.mapping.stats


import com.google.gson.annotations.SerializedName

data class Downloads(
    @SerializedName("historical")
    val historical: Historical,
    @SerializedName("total")
    val total: Int
)
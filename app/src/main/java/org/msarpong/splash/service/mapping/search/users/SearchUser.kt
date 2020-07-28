package org.msarpong.splash.service.mapping.search.users


import com.google.gson.annotations.SerializedName

data class SearchUser(
    @SerializedName("results")
    val results: List<Result>,
    @SerializedName("total")
    val total: Int,
    @SerializedName("total_pages")
    val totalPages: Int
)
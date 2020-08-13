package org.msarpong.photology.service.mapping.like


import com.google.gson.annotations.SerializedName

data class LikePhotoResponse(
    @SerializedName("photo")
    val photo: Photo,
    @SerializedName("user")
    val user: User
)
package org.msarpong.splash.service.mapping.detail_photo


import com.google.gson.annotations.SerializedName

data class UserXX(
    @SerializedName("accepted_tos")
    val acceptedTos: Boolean,
    @SerializedName("bio")
    val bio: Any,
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("instagram_username")
    val instagramUsername: Any,
    @SerializedName("last_name")
    val lastName: String,
    @SerializedName("links")
    val links: LinksXXXXXX,
    @SerializedName("location")
    val location: Any,
    @SerializedName("name")
    val name: String,
    @SerializedName("portfolio_url")
    val portfolioUrl: Any,
    @SerializedName("profile_image")
    val profileImage: ProfileImageXX,
    @SerializedName("total_collections")
    val totalCollections: Int,
    @SerializedName("total_likes")
    val totalLikes: Int,
    @SerializedName("total_photos")
    val totalPhotos: Int,
    @SerializedName("twitter_username")
    val twitterUsername: Any,
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("username")
    val username: String
)
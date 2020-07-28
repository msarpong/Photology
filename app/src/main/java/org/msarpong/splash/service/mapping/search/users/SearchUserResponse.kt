package org.msarpong.splash.service.mapping.search.users


import com.google.gson.annotations.SerializedName

data class SearchUserResponse(
    @SerializedName("results")
    val results: List<Result>,
    @SerializedName("total")
    val total: Int,
    @SerializedName("total_pages")
    val totalPages: Int
) {
    data class Result(
        @SerializedName("accepted_tos")
        val acceptedTos: Boolean,
        @SerializedName("bio")
        val bio: Any,
        @SerializedName("first_name")
        val firstName: String,
        @SerializedName("followed_by_user")
        val followedByUser: Boolean,
        @SerializedName("id")
        val id: String,
        @SerializedName("instagram_username")
        val instagramUsername: String,
        @SerializedName("last_name")
        val lastName: String,
        @SerializedName("links")
        val links: Links,
        @SerializedName("location")
        val location: String,
        @SerializedName("name")
        val name: String,
        @SerializedName("photos")
        val photos: List<Photo>,
        @SerializedName("portfolio_url")
        val portfolioUrl: Any,
        @SerializedName("profile_image")
        val profileImage: ProfileImage,
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
}
package org.msarpong.splash.service.mapping.profile


import com.google.gson.annotations.SerializedName

data class Profile(
    @SerializedName("accepted_tos")
    val acceptedTos: Boolean,
    @SerializedName("allow_messages")
    val allowMessages: Boolean,
    @SerializedName("badge")
    val badge: Any,
    @SerializedName("bio")
    val bio: String,
    @SerializedName("downloads")
    val downloads: Int,
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("followed_by_user")
    val followedByUser: Boolean,
    @SerializedName("followers_count")
    val followersCount: Int,
    @SerializedName("following_count")
    val followingCount: Int,
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
    @SerializedName("meta")
    val meta: Meta,
    @SerializedName("name")
    val name: String,
    @SerializedName("numeric_id")
    val numericId: Int,
    @SerializedName("photos")
    val photos: List<Photo>,
    @SerializedName("portfolio_url")
    val portfolioUrl: String,
    @SerializedName("profile_image")
    val profileImage: ProfileImage,
    @SerializedName("tags")
    val tags: Tags,
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
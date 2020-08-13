package org.msarpong.photology.service.mapping.search

import com.google.gson.annotations.SerializedName
import org.msarpong.photology.service.mapping.collection.LinksX
import org.msarpong.photology.service.mapping.collection.ProfileImage

data class SearchResponse(
    @SerializedName("results")
    val results: List<Result>,
    @SerializedName("total")
    val total: Int,
    @SerializedName("total_pages")
    val totalPages: Int
) {
    data class UserResult(
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
        @SerializedName("location")
        val location: String,
        @SerializedName("name")
        val name: String,
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

    data class Result(
        @SerializedName("alt_description")
        val altDescription: String,
        @SerializedName("categories")
        val categories: List<Any>,
        @SerializedName("color")
        val color: String,
        @SerializedName("created_at")
        val createdAt: String,
        @SerializedName("current_user_collections")
        val currentUserCollections: List<Any>,
        @SerializedName("description")
        val description: String,
        @SerializedName("height")
        val height: Int,
        @SerializedName("id")
        val id: String,
        @SerializedName("liked_by_user")
        val likedByUser: Boolean,
        @SerializedName("likes")
        val likes: Int,
        @SerializedName("promoted_at")
        val promotedAt: String,
        @SerializedName("sponsorship")
        val sponsorship: Any,
        @SerializedName("updated_at")
        val updatedAt: String,
        @SerializedName("width")
        val width: Int,
        @SerializedName("urls")
        val urls: Urls,
        @SerializedName("user")
        val user: User
    ) {
        data class Urls(
            @SerializedName("full")
            val full: String,
            @SerializedName("raw")
            val raw: String,
            @SerializedName("regular")
            val regular: String,
            @SerializedName("small")
            val small: String,
            @SerializedName("thumb")
            val thumb: String
        )

        data class User(
            @SerializedName("accepted_tos")
            val acceptedTos: Boolean,
            @SerializedName("bio")
            val bio: String,
            @SerializedName("first_name")
            val firstName: String,
            @SerializedName("id")
            val id: String,
            @SerializedName("instagram_username")
            val instagramUsername: String,
            @SerializedName("last_name")
            val lastName: String,
            @SerializedName("links")
            val links: LinksX,
            @SerializedName("location")
            val location: String,
            @SerializedName("name")
            val name: String,
            @SerializedName("portfolio_url")
            val portfolioUrl: String,
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

}
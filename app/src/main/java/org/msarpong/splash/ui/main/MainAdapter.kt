package org.msarpong.splash.ui.main

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.msarpong.splash.R
import org.msarpong.splash.service.mapping.Unsplash
import org.msarpong.splash.service.mapping.UnsplashItem

class MainAdapter : ListAdapter<UnsplashItem, UnsplashViewHolder>(UnsplashDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UnsplashViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.main_image_recycler, parent, false)
        return UnsplashViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: UnsplashViewHolder, position: Int) {
        val picture = getItem(position)
        picture.let {

            holder.userName.text = picture.user.username

            Glide
                .with(holder.userImage.context)
                .load(picture.user.profileImage.small)
                .fitCenter()
                .into(holder.userImage)

            Glide
                .with(holder.image.context)
                .load(picture.urls.small)
                .fitCenter()
                .into(holder.image)
        }
    }

}

class UnsplashViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val image: ImageView = itemView.findViewById(R.id.image_cover)
    val userImage: ImageView = itemView.findViewById(R.id.image_user)
    val userName: TextView = itemView.findViewById(R.id.text_username)
}

class UnsplashDiffUtil : DiffUtil.ItemCallback<UnsplashItem>() {
    override fun areItemsTheSame(oldItem: UnsplashItem, newItem: UnsplashItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: UnsplashItem, newItem: UnsplashItem): Boolean {
        return oldItem == newItem
    }
}
package org.msarpong.splash.ui.main

import android.app.Activity
import android.util.Log
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
import org.msarpong.splash.service.mapping.photos.PhotoResponseItem
import org.msarpong.splash.ui.detail_photo.DetailPhotoScreen
import org.msarpong.splash.ui.profile.ProfilePhotoScreen

class MainAdapter : ListAdapter<PhotoResponseItem, UnsplashViewHolder>(UnsplashDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UnsplashViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.main_image_recycler, parent, false)
        return UnsplashViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: UnsplashViewHolder, position: Int) {
        val picture = getItem(position)
        Log.d("PositionRec", position.toString())
        val array = arrayOf(picture.sponsorship)[0]
//        Log.d("Sponsor_SIZE", array.size.toString())


        if (array?.impressionUrls.isNullOrEmpty()){
            Log.d("Sponsor_ELSE", "${position} is  NOT a sponsor photo")
            picture.sponsorship.let {
                holder.userName.text = picture.user.username
                holder.userName.setOnClickListener {
                    ProfilePhotoScreen.openPhotoProfile(
                        holder.userName.context as Activity,
                        picture.user.username
                    )
                }

                holder.image.setOnClickListener {
                    DetailPhotoScreen.openDetailPhoto(holder.image.context as Activity, picture.id)
                }

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
        }else{
            Log.d("Sponsor_IF", "${position} is a sponsor photo ")
            holder.image.visibility = View.GONE
            holder.userImage.visibility = View.GONE
            holder.userName.visibility = View.GONE
            holder.userView.visibility = View.GONE
        }



    }


}

class UnsplashViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val image: ImageView = itemView.findViewById(R.id.image_cover)
    val userImage: ImageView = itemView.findViewById(R.id.image_user)
    val userName: TextView = itemView.findViewById(R.id.text_username)
    val userView: View = itemView.findViewById(R.id.view)
}

class UnsplashDiffUtil : DiffUtil.ItemCallback<PhotoResponseItem>() {
    override fun areItemsTheSame(oldItem: PhotoResponseItem, newItem: PhotoResponseItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: PhotoResponseItem,
        newItem: PhotoResponseItem
    ): Boolean {
        return oldItem == newItem
    }
}
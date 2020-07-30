package org.msarpong.splash.ui.following

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
import org.msarpong.splash.service.mapping.following.FollowingResponseItem

class FollowingAdapter :
    ListAdapter<FollowingResponseItem, FollowingViewHolder>(FollowingDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowingViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.recycler_following_user, parent, false)
        return FollowingViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: FollowingViewHolder, position: Int) {
        val user = getItem(position)
        user.let {
            holder.usernameUser.text = user.username
            Glide
                .with(holder.imageUser.context)
                .load(user.profileImage.small)
                .fitCenter()
                .into(holder.imageUser)
        }
    }
}

class FollowingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val imageUser: ImageView = itemView.findViewById(R.id.image_user)
    val usernameUser: TextView = itemView.findViewById(R.id.text_username)
}

class FollowingDiffUtil : DiffUtil.ItemCallback<FollowingResponseItem>() {
    override fun areItemsTheSame(
        oldItem: FollowingResponseItem,
        newItem: FollowingResponseItem
    ): Boolean {
        TODO("Not yet implemented")
    }

    override fun areContentsTheSame(
        oldItem: FollowingResponseItem,
        newItem: FollowingResponseItem
    ): Boolean {
        TODO("Not yet implemented")
    }

}

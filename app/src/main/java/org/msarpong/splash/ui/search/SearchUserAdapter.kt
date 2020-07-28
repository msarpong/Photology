package org.msarpong.splash.ui.search

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
import org.msarpong.splash.service.mapping.search.SearchResponse
import org.msarpong.splash.service.mapping.search.users.SearchUserResponse

class SearchUserAdapter :
    ListAdapter<SearchUserResponse.Result, SearchUserViewHolder>(SearchUserDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchUserViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.recycler_search_user, parent, false)
        return SearchUserViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: SearchUserViewHolder, position: Int) {
        val resultUser = getItem(position)
        resultUser.let {
            Glide
                .with(holder.profileImage.context)
                .load(resultUser.profileImage.large)
                .fitCenter()
                .into(holder.profileImage)

            holder.profileUsername.text = resultUser.username
            holder.profileName.text = resultUser.name
        }

    }

}

class SearchUserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val profileImage: ImageView = itemView.findViewById(R.id.result_profile_image)
    val profileUsername: TextView = itemView.findViewById(R.id.result_username)
    val profileName: TextView = itemView.findViewById(R.id.result_name)

}

class SearchUserDiffUtil : DiffUtil.ItemCallback<SearchUserResponse.Result>() {
    override fun areItemsTheSame(
        oldItem: SearchUserResponse.Result,
        newItem: SearchUserResponse.Result
    ): Boolean {
        return oldItem.id == newItem.id

    }

    override fun areContentsTheSame(
        oldItem: SearchUserResponse.Result,
        newItem: SearchUserResponse.Result
    ): Boolean {
        return oldItem == newItem
    }

}
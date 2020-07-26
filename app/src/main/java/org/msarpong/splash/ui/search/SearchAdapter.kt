package org.msarpong.splash.ui.search

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.msarpong.splash.R
import org.msarpong.splash.service.mapping.search.SearchResponse
import org.msarpong.splash.ui.detail_photo.DetailPhotoScreen

class SearchAdapter : ListAdapter<SearchResponse.Result, SearchViewHolder>(SearchDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.recycler_search_grid_photo, parent, false)
        return SearchViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val picture = getItem(position)
        picture.let {

            holder.image.setOnClickListener {
                DetailPhotoScreen.openDetailPhoto(holder.image.context as Activity, picture.id)
            }

            Glide
                .with(holder.image.context)
                .load(picture.urls.small)
                .fitCenter()
                .into(holder.image)
        }
    }

}

class SearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val image: ImageView = itemView.findViewById(R.id.image_cover)
}

class SearchDiffUtil : DiffUtil.ItemCallback<SearchResponse.Result>() {
    override fun areItemsTheSame(
        oldItem: SearchResponse.Result,
        newItem: SearchResponse.Result
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: SearchResponse.Result,
        newItem: SearchResponse.Result
    ): Boolean {
        return oldItem == newItem
    }
}

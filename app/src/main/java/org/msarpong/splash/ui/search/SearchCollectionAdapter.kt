package org.msarpong.splash.ui.search

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.msarpong.splash.R
import org.msarpong.splash.service.mapping.search.collections.SearchCollectionResponse
import org.msarpong.splash.ui.collections.CollectionPhotoScreen
import org.msarpong.splash.ui.detail_photo.DetailPhotoScreen

class SearchCollectionAdapter :
    ListAdapter<SearchCollectionResponse.Result, SearchCollectionViewHolder>(
        SearchCollectionDiffUtil()
    ) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchCollectionViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.recycler_collection, parent, false)
        return SearchCollectionViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: SearchCollectionViewHolder, position: Int) {
        val collectionResult: SearchCollectionResponse.Result = getItem(position)
        collectionResult.let {

            holder.collectionTitle.text = collectionResult.title
            holder.collectionTotal.text = collectionResult.totalPhotos.toString()

            showImage(collectionResult, holder)
            buttonListener(collectionResult, holder)
        }
    }

    private fun buttonListener(
        collection: SearchCollectionResponse.Result,
        holder: SearchCollectionViewHolder
    ) {

        holder.collectionContainer.setOnClickListener {
            CollectionPhotoScreen.openPhotoCollection(
                holder.collectionContainer.context as Activity, collection.id.toString()
            )
        }

        holder.collectionMain.setOnClickListener {
            DetailPhotoScreen.openDetailPhoto(
                holder.collectionMain.context as Activity,
                collection.previewPhotos[0].id
            )
        }
        holder.collectionTop.setOnClickListener {
            DetailPhotoScreen.openDetailPhoto(
                holder.collectionMain.context as Activity,
                collection.previewPhotos[1].id
            )
        }

        holder.collectionBottom.setOnClickListener {
            DetailPhotoScreen.openDetailPhoto(
                holder.collectionMain.context as Activity,
                collection.previewPhotos[2].id
            )
        }
    }

    private fun showImage(
        collection: SearchCollectionResponse.Result,
        holder: SearchCollectionViewHolder
    ) {
        if (collection.previewPhotos.size >= 3) {
            Glide
                .with(holder.collectionMain.context)
                .load(collection.previewPhotos[0].urls.regular)
                .fitCenter()
                .into(holder.collectionMain)

            Glide
                .with(holder.collectionTop.context)
                .load(collection.previewPhotos[1].urls.regular)
                .fitCenter()
                .into(holder.collectionTop)

            Glide
                .with(holder.collectionBottom.context)
                .load(collection.previewPhotos[2].urls.regular)
                .fitCenter()
                .into(holder.collectionBottom)
        } else {
            holder.collectionBottom.visibility = View.INVISIBLE
            holder.collectionTop.visibility = View.INVISIBLE
//            holder.collectionMain.layoutParams = RelativeLayout.LayoutParams(MATCH_PARENT,720)
            holder.collectionMain.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT

        }

    }

}

class SearchCollectionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val collectionMain: ImageView = itemView.findViewById(R.id.collection_main)
    val collectionTop: ImageView = itemView.findViewById(R.id.collection_top)
    val collectionBottom: ImageView = itemView.findViewById(R.id.collection_bottom)
    val collectionTitle: TextView = itemView.findViewById(R.id.collection_text_title)
    val collectionTotal: TextView = itemView.findViewById(R.id.collection_text_total)
    val collectionContainer: LinearLayout = itemView.findViewById(R.id.collection_container)
}

class SearchCollectionDiffUtil : DiffUtil.ItemCallback<SearchCollectionResponse.Result>() {
    override fun areItemsTheSame(
        oldItem: SearchCollectionResponse.Result,
        newItem: SearchCollectionResponse.Result
    ): Boolean {
        return oldItem.id == newItem.id

    }

    override fun areContentsTheSame(
        oldItem: SearchCollectionResponse.Result,
        newItem: SearchCollectionResponse.Result
    ): Boolean {
        return oldItem == newItem
    }

}
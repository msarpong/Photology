package org.msarpong.photology.ui.collections

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
import org.msarpong.photology.R
import org.msarpong.photology.service.mapping.collection.CollectionItem
import org.msarpong.photology.ui.detail_photo.DetailPhotoScreen

class CollectionAdapter : ListAdapter<CollectionItem, CollectionViewHolder>(CollectionDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectionViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.recycler_collection, parent, false)
        return CollectionViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: CollectionViewHolder, position: Int) {
        val picture: CollectionItem = getItem(position)
        picture.let {

            holder.collectionTitle.text = picture.title
            holder.collectionTotal.text = picture.totalPhotos.toString()

            holder.collectionMain.setOnClickListener {
                DetailPhotoScreen.openDetailPhoto(
                    holder.collectionMain.context as Activity,
                    picture.previewPhotos[0].id
                )
            }
            holder.collectionTop.setOnClickListener {
                DetailPhotoScreen.openDetailPhoto(
                    holder.collectionMain.context as Activity,
                    picture.previewPhotos[1].id
                )
            }

            holder.collectionBottom.setOnClickListener {
                DetailPhotoScreen.openDetailPhoto(
                    holder.collectionMain.context as Activity,
                    picture.previewPhotos[2].id
                )
            }
            showImage(picture, holder)
            buttonListener(picture, holder)
        }

    }

    private fun buttonListener(picture: CollectionItem, holder: CollectionViewHolder) {
        holder.collectionMain.setOnClickListener {
            DetailPhotoScreen.openDetailPhoto(
                holder.collectionMain.context as Activity,
                picture.previewPhotos[0].id
            )
        }
        holder.collectionTop.setOnClickListener {
            DetailPhotoScreen.openDetailPhoto(
                holder.collectionMain.context as Activity,
                picture.previewPhotos[1].id
            )
        }

        holder.collectionBottom.setOnClickListener {
            DetailPhotoScreen.openDetailPhoto(
                holder.collectionMain.context as Activity,
                picture.previewPhotos[2].id
            )
        }

        holder.collectionContainer.setOnClickListener {
            CollectionPhotoScreen.openPhotoCollection(
                holder.collectionContainer.context as Activity, picture.id.toString()
            )
        }
    }

    private fun showImage(picture: CollectionItem, holder: CollectionViewHolder) {
        Glide
            .with(holder.collectionMain.context)
            .load(picture.previewPhotos[0].urls.regular)
            .fitCenter()
            .into(holder.collectionMain)

        Glide
            .with(holder.collectionTop.context)
            .load(picture.previewPhotos[1].urls.regular)
            .fitCenter()
            .into(holder.collectionTop)

        Glide
            .with(holder.collectionBottom.context)
            .load(picture.previewPhotos[2].urls.regular)
            .fitCenter()
            .into(holder.collectionBottom)
    }
}

class CollectionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val collectionMain: ImageView = itemView.findViewById(R.id.collection_main)
    val collectionTop: ImageView = itemView.findViewById(R.id.collection_top)
    val collectionBottom: ImageView = itemView.findViewById(R.id.collection_bottom)
    val collectionTitle: TextView = itemView.findViewById(R.id.collection_text_title)
    val collectionTotal: TextView = itemView.findViewById(R.id.collection_text_total)
    val collectionContainer: View = itemView.findViewById(R.id.collection_container)
}

class CollectionDiffUtil : DiffUtil.ItemCallback<CollectionItem>() {
    override fun areItemsTheSame(oldItem: CollectionItem, newItem: CollectionItem): Boolean {
        return oldItem.title == newItem.title
    }

    override fun areContentsTheSame(oldItem: CollectionItem, newItem: CollectionItem): Boolean {
        return oldItem == newItem
    }
}
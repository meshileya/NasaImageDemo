package com.nasa.demo.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingData
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nasa.demo.R
import com.nasa.demo.databinding.ListingImagesBinding
import com.nasa.demo.domain.model.NasaImageUIItem

class NasaImageAdapter(
    private val onItemClick: (NasaImageUIItem) -> Unit
) : PagingDataAdapter<NasaImageUIItem, NasaImageAdapter.NasaImageViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NasaImageViewHolder {
        val binding = ListingImagesBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return NasaImageViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: NasaImageViewHolder, position: Int) {
        getItem(position)?.let { item ->
            holder.bind(item)
        }
    }

    class NasaImageViewHolder(
        private val binding: ListingImagesBinding,
        private val onItemClick: (NasaImageUIItem) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: NasaImageUIItem) {
            // Set the title with formatted string
            binding.tvTitle.text = itemView.context.getString(R.string.earth_id, item.id.toString())

            // Set the favorite icon based on the item's favorite status
            binding.ivFavorite.setImageResource(
                if (item.isFavorite) R.drawable.ic_favorite_24
                else R.drawable.ic_favorite_border_24
            )

            // Load the image using Glide
            Glide.with(binding.root.context)
                .load(item.imageUrl)
                .error(R.drawable.ic_placeholder)
                .into(binding.ivIcon)

            // Set the item click listener
            itemView.setOnClickListener {
                onItemClick(item.copy(isFavorite = !item.isFavorite))
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<NasaImageUIItem>() {
            override fun areItemsTheSame(oldItem: NasaImageUIItem, newItem: NasaImageUIItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: NasaImageUIItem, newItem: NasaImageUIItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}

package com.nasa.demo.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nasa.demo.R
import com.nasa.demo.databinding.ListingFavoritesBinding
import com.nasa.demo.domain.model.NasaImageUIItem

class NasaFavoriteAdapter : RecyclerView.Adapter<NasaFavoriteAdapter.CustomViewHolder>() {

    private val items: MutableList<NasaImageUIItem> = mutableListOf()

    // Updates the list and notifies specific changes
    fun updateList(newItems: List<NasaImageUIItem>) {
        val diffResult = DiffUtil.calculateDiff(NasaImageDiffCallback(items, newItems))
        items.clear()
        items.addAll(newItems)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val binding = ListingFavoritesBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return CustomViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    // ViewHolder class to hold and bind views
    class CustomViewHolder(private val binding: ListingFavoritesBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: NasaImageUIItem) {
            Glide.with(binding.root.context)
                .load(item.imageUrl)
                .error(R.drawable.ic_placeholder)
                .into(binding.ivIcon)
        }
    }

    // DiffUtil Callback to compare old and new items
    class NasaImageDiffCallback(
        private val oldList: List<NasaImageUIItem>,
        private val newList: List<NasaImageUIItem>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}

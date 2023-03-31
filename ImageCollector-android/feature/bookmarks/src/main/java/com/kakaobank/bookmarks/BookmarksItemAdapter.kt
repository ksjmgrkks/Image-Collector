package com.kakaobank.bookmarks

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.kakaobank.bookmarks.databinding.ItemBookmarksBinding
import com.kakaobank.core.data.SearchItem

class BookmarksItemAdapter(
    private val onClick: (SearchItem, BookmarksItemAdapter) -> Unit
) : ListAdapter<SearchItem, BookmarksItemAdapter.BookmarksItemViewHolder>(diffUtil) {
    inner class BookmarksItemViewHolder(
        private val binding: ItemBookmarksBinding,
        private val onClick: (SearchItem, BookmarksItemAdapter) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SearchItem) {
            /* Glide 이미지 로딩 Progress */
            val circularProgressDrawable = CircularProgressDrawable(binding.root.context)
            circularProgressDrawable.strokeWidth = 5f
            circularProgressDrawable.centerRadius = 30f
            circularProgressDrawable.start()

            Glide
                .with(binding.root.context)
                .load(item.imageUrl)
                .transition(DrawableTransitionOptions.withCrossFade())
                .placeholder(circularProgressDrawable)
                .into(binding.imageViewBookmarks)

            binding.frameLayoutBookmarks.setOnClickListener {
                onClick(item, this@BookmarksItemAdapter)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookmarksItemViewHolder {
        return BookmarksItemViewHolder(
            binding = ItemBookmarksBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onClick = onClick
        )
    }

    override fun onBindViewHolder(holder: BookmarksItemViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    fun removeItem(imageUrl: String) : Boolean {
        val newList = currentList.toMutableList()
        newList.forEachIndexed { index, item ->
            if(item.imageUrl == imageUrl){
                newList.removeAt(index)
                submitList(newList)
                return true
            }
        }
        return false
    }

    //링크: https://developer.android.com/reference/androidx/recyclerview/widget/DiffUtil
    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<SearchItem>() {
            override fun areItemsTheSame(oldItem: SearchItem, newItem: SearchItem): Boolean {
                return oldItem.imageUrl == newItem.imageUrl
            }

            override fun areContentsTheSame(oldItem: SearchItem, newItem: SearchItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
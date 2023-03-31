package com.kakaobank.search

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.kakaobank.core.data.SearchItem
import com.kakaobank.core.data.datetimeText
import com.kakaobank.search.databinding.ItemSearchBinding

class SearchItemAdapter(
    private val onClick : (SearchItem) -> Unit
) : PagingDataAdapter<SearchItem, SearchItemAdapter.SearchItemViewHolder>(ITEM_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchItemViewHolder =
        SearchItemViewHolder(
            binding = ItemSearchBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            ),
            onClick = onClick
        )

    override fun onBindViewHolder(holder: SearchItemViewHolder, position: Int) {
        val item = getItem(position)
        item?.run {
            holder.bind(this, position)
        }
    }

    inner class SearchItemViewHolder(
        private val binding: ItemSearchBinding,
        private val onClick: (SearchItem) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SearchItem, position: Int) {
            binding.apply {
                val circularProgressDrawable = CircularProgressDrawable(binding.root.context)
                circularProgressDrawable.strokeWidth = 5f
                circularProgressDrawable.centerRadius = 30f
                circularProgressDrawable.start()

                binding.textViewDatetime.text = item.datetimeText
                Glide
                    .with(binding.root.context)
                    .load(item.imageUrl)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .placeholder(circularProgressDrawable)
                    .into(binding.imageViewSearch)

                if(item.bookmarked){
                    binding.lottieAnimationViewBookmark.visibility = View.VISIBLE
                }else{
                    binding.lottieAnimationViewBookmark.visibility = View.GONE
                }

                binding.cardViewSearch.setOnClickListener {
                    binding.lottieAnimationViewBookmark.playAnimation()
                    onClick(item)
                    bookmarkChange(
                        item = item,
                        position = position
                    )
                }
            }
        }
    }

    fun bookmarkChange(item: SearchItem, position: Int){
        val snapshotSearchItem = this@SearchItemAdapter.snapshot().firstOrNull { snapshotItem ->
            snapshotItem?.imageUrl == item.imageUrl
        }
        if(snapshotSearchItem != null) {
            snapshotSearchItem.bookmarked = !snapshotSearchItem.bookmarked
            this@SearchItemAdapter.notifyItemChanged(position)
        }
    }

    fun bookmarkChange(imageUrl: String){
        val snapshotSearchItem = this@SearchItemAdapter.snapshot().firstOrNull { snapshotItem ->
            snapshotItem?.imageUrl == imageUrl
        }

        if(snapshotSearchItem != null) {
            val position = this@SearchItemAdapter.snapshot().indexOf(snapshotSearchItem)
            snapshotSearchItem.bookmarked = !snapshotSearchItem.bookmarked
            this@SearchItemAdapter.notifyItemChanged(position)
        }
    }

    companion object {
        private val ITEM_COMPARATOR = object : DiffUtil.ItemCallback<SearchItem>() {
            override fun areItemsTheSame(oldItem: SearchItem, newItem: SearchItem): Boolean {
                return oldItem.imageUrl == newItem.imageUrl
            }

            override fun areContentsTheSame(oldItem: SearchItem, newItem: SearchItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
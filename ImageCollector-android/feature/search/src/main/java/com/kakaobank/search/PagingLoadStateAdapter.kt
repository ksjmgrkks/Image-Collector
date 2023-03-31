package com.kakaobank.search

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kakaobank.search.databinding.ItemLoadStateBinding

class PagingLoadStateAdapter(
    private val retryCallback: () -> Unit,
    private val isLastPageCallback: (Boolean) -> Unit
) : LoadStateAdapter<PagingLoadStateViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): PagingLoadStateViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return PagingLoadStateViewHolder(ItemLoadStateBinding.inflate(layoutInflater, parent, false), retryCallback, isLastPageCallback)
    }

    override fun onBindViewHolder(holder: PagingLoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }
}

class PagingLoadStateViewHolder(
    private val binding: ItemLoadStateBinding,
    private val retry: () -> Unit,
    private val isLastPage: (Boolean) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    /* Paging Error시 아이템에서 보여줄 View */
    fun bind(state: LoadState) {
        binding.buttonRetry.setOnClickListener { retry() }
        when(state){
            is LoadState.Loading -> {
                binding.progressBarLoading.visibility = View.VISIBLE
            }
            is LoadState.Error -> {
                binding.buttonRetry.visibility = View.VISIBLE
                binding.progressBarLoading.visibility = View.GONE
                Log.d(this.javaClass.name, state.error.message ?: "")
            }
            else -> {
                binding.buttonRetry.visibility = View.GONE
                binding.progressBarLoading.visibility = View.GONE
            }
        }
        isLastPage(state.endOfPaginationReached) /* endOfPaginationReached는 페이지 끝에 도달하면 true가 됨 */
    }
}
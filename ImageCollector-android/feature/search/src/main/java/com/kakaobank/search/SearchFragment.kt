package com.kakaobank.search
import android.util.Log

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import com.kakaobank.core.ui.ActivityViewModel
import com.kakaobank.core.ui.BindingFragment
import com.kakaobank.core.util.*
import com.kakaobank.search.databinding.FragmentSearchBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.net.UnknownHostException

@AndroidEntryPoint
class SearchFragment : BindingFragment<FragmentSearchBinding>(R.layout.fragment_search) {
    private val activityViewModel: ActivityViewModel by activityViewModels() /* Fragment간 정보 교환을 위한 activityViewModel */
    private val viewModel: SearchViewModel by viewModels()
    private val adapter = SearchItemAdapter(
        onClick = {
            activity?.run {
                /* Fragment가 attached된 Activity가 finishing 되는 상황이면 return */
                if (isFinishing)
                    return@run

                if (it.bookmarked) {
                    val result = applicationContext.removeImageFromList(it)
                    if (result)
                        showToast(
                            this,
                            getString(com.kakaobank.core.R.string.bookmarks_remove_image)
                        )
                    else
                        showToast(
                            this,
                            getString(com.kakaobank.core.R.string.bookmarks_remove_image)
                        )
                } else {
                    showToast(this, getString(com.kakaobank.core.R.string.bookmarks_add_image))
                    addImageToList(it)
                }
            }
        }
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerViewSearch.adapter = adapter.
        /* 페이징 추가 페이지 호출 중 오류 났을 때 처리를 위함 */
        withLoadStateHeaderAndFooter(
            PagingLoadStateAdapter({ adapter.retry() }, /* Header 부분에서 오류시 retry 버튼을 누르면 재요청 */
                { }),
            PagingLoadStateAdapter({ adapter.retry() },  /* Footer 부분에서 오류시 retry 버튼을 누르면 재요청 */
                { isLastPage ->
                    if (isLastPage)
                        getString(com.kakaobank.core.R.string.search_paging_last_page_message)
                } /* Footer 부분에서 마지막 페이지 호출시 안내 토스트 메시지 */
            )
        )
        setSearchView()
        setUiStateObserver()
        setSearchItemFlowObserver()
    }

    private fun setSearchView(){
        /* searchView(검색 UI) Setting */
        binding.searchView.setIconifiedByDefault(false)
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.run {
                    viewModel.firstSearchImages(this)
                }
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    private fun setUiStateObserver(){
        /* UIState Observer */
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collectLatest { uiState ->
                    /* 초기 가이드 메시지 */
                    if (uiState.isGuideMessageVisible) {
                        binding.textViewGuide.visibility = View.VISIBLE
                    } else {
                        binding.textViewGuide.visibility = View.GONE
                    }

                    /* ProgressBar */
                    if (uiState.isLoading) {
                        showProgressBar()
                    } else {
                        hideProgressBar()
                    }
                }
            }
        }

        /* 페이징 Error handling */
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                adapter.loadStateFlow.collectLatest {
                    when(val currentState = it.refresh){
                        is LoadState.Error -> {
                            when(currentState.error){
                                is UnknownHostException -> {
                                    Log.d(this.javaClass.name, currentState.error.message.orEmpty())
                                    showToast(requireContext(), getString(com.kakaobank.core.R.string.search_paging_network_message))
                                    viewModel.setProgressBar(false)
                                }

                                is IndexOutOfBoundsException -> {
                                    Log.d(this.javaClass.name, currentState.error.message.orEmpty())
                                    showToast(requireContext(), getString(com.kakaobank.core.R.string.search_paging_no_search_message))
                                    viewModel.setProgressBar(false)
                                }

                                else -> {
                                    Log.d(this.javaClass.name, currentState.error.message.orEmpty())
                                    showToast(requireContext(), getString(com.kakaobank.core.R.string.search_paging_error_message))
                                    viewModel.setProgressBar(false)
                                }
                            }
                        }
                        else -> { /* LoadState.NotLoading(불러온 상태), LoadState.Loading(로딩중 상태) */ }
                    }
                }
            }
        }

        /* 내 보관함에서 아이템 삭제시 북마크 상태 변경을 위한 Observer */
        lifecycleScope.launch {
            activityViewModel.imageUrl.collectLatest {
                it.getContentIfNotHandled()?.run {
                    adapter.bookmarkChange(this)
                }
            }
        }
    }

    private fun setSearchItemFlowObserver() {
        /* 첫번째 아이템 Observer */
        lifecycleScope.launch {
            viewModel.firstPagingData.collectLatest {
                viewModel.setProgressBar(false)
                viewModel.searchImages()
                adapter.submitData(it)
            }
        }

        /* 나머지 아이템 Observer */
        lifecycleScope.launch {
            viewModel.pagingData.collectLatest {
                adapter.submitData(it)
            }
        }
    }

    private fun showProgressBar() {
        binding.recyclerViewSearch.visibility = View.GONE
        binding.constraintLayoutProgressBarTotal.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.recyclerViewSearch.visibility = View.VISIBLE
        binding.constraintLayoutProgressBarTotal.visibility = View.GONE
    }
}
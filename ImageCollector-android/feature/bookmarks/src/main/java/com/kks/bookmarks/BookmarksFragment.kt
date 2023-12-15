package com.kks.bookmarks

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.kks.bookmarks.databinding.FragmentBookmarksBinding
import com.kks.core.ui.ActivityViewModel
import com.kks.core.ui.BindingFragment
import com.kks.core.util.Constants
import com.kks.core.util.getImageList
import com.kks.core.util.removeImageFromList
import com.kks.core.util.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BookmarksFragment : BindingFragment<FragmentBookmarksBinding>(R.layout.fragment_bookmarks) {

    private val activityViewModel: ActivityViewModel by activityViewModels() /* Fragment간 정보 교환을 위한 activityViewModel */

    private val adapter = BookmarksItemAdapter(
        /* 내 보관함 탭에서 아이템을 클릭했을 때 */
        onClick = { item, adapter ->
            activity?.run {
                /* Fragment가 attached된 Activity가 finishing 되는 상황이면 return */
                if (isFinishing)
                    return@run

                val builder = AlertDialog.Builder(this)
                builder.setTitle(getString(com.kks.core.R.string.bookmarks_remove_dialog_title))
                    .setMessage(getString(com.kks.core.R.string.bookmarks_remove_dialog_description))
                    .setPositiveButton(getString(com.kks.core.R.string.bookmarks_remove_dialog_yes)) { _, _ ->
                        if (removeImageFromList(item)) {
                            if (adapter.removeItem(item.imageUrl)) {
                                showToast(
                                    this,
                                    getString(com.kks.core.R.string.bookmarks_remove_image)
                                )
                                activityViewModel.removeImage(item.imageUrl)
                                if (getImageList(Constants.BOOKMARKED_LIST).size == 0)
                                    binding.textViewBookmarks.visibility = View.VISIBLE
                            } else
                                showToast(
                                    this,
                                    getString(com.kks.core.R.string.bookmarks_do_not_have_image)
                                )
                        } else
                            showToast(
                                this,
                                getString(com.kks.core.R.string.bookmarks_do_not_have_image)
                            )
                    }
                    .setNegativeButton(getString(com.kks.core.R.string.bookmarks_remove_dialog_no)) { _, _ -> }
                builder.show()
            }
        }
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /* 초기 뷰 세팅 */
        binding.recyclerViewBookmarks.adapter = adapter
        activity?.run {
            /* Fragment가 attached된 Activity가 finishing 되는 상황이면 return */
            if (isFinishing)
                return@run

            val bookmarksList = getImageList(Constants.BOOKMARKED_LIST)
            if (bookmarksList.size == 0)
                binding.textViewBookmarks.visibility = View.VISIBLE
            else
                binding.textViewBookmarks.visibility = View.GONE
            adapter.submitList(bookmarksList)
        }

        /* 검색 탭에서 아이템 추가시 북마크 화면 안내 메시지를 사라지게 하기위한 Observer */
        viewLifecycleOwner.lifecycleScope.launch {
            activityViewModel.imageUrl.collectLatest {
                it.getContentIfNotHandled()?.run {
                    binding.textViewBookmarks.visibility = View.GONE
                }
            }
        }
    }
}


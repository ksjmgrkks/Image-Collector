package com.kakaobank.core.ui

import androidx.lifecycle.viewModelScope
import com.kakaobank.core.util.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ActivityViewModel @Inject constructor(
) : BaseViewModel() {
    private val _imageUrl = MutableSharedFlow<Event<String>>()
    val imageUrl = _imageUrl.asSharedFlow()

    fun removeImage(imageUrl: String) = viewModelScope.launch {
        _imageUrl.emit(Event(imageUrl))
    }
}
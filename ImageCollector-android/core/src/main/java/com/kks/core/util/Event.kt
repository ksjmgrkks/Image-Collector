package com.kks.core.util

class Event<out T>(private val content: T) {

    private var hasBeenHandled = false

    /**
     * content를 반환하고 이를 재사용하는 것을 방지합니다.
     */
    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }
}
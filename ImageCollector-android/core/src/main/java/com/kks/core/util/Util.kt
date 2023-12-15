package com.kks.core.util

import android.content.Context
import android.widget.Toast
import com.kks.core.data.SearchItem
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

fun Context.saveImageList(arrayList: ArrayList<SearchItem>, key: String) {
    val editor = getSharedPreferences(Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE).edit()
    val adapter = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        .adapter<List<SearchItem>>(Types.newParameterizedType(List::class.java, SearchItem::class.java))
    editor.putString(key, adapter.toJson(arrayList))
    editor.apply()
}

fun Context.getImageList(key: String): ArrayList<SearchItem> {
    val json = getSharedPreferences(Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE).getString(key, null) ?: return arrayListOf()
    val adapter = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        .adapter<List<SearchItem>>(Types.newParameterizedType(List::class.java, SearchItem::class.java))
    return adapter.fromJson(json)?.let { ArrayList(it) } ?: arrayListOf()
}

fun Context.addImageToList(item: SearchItem) {
    val list = getImageList(Constants.BOOKMARKED_LIST)
    list.add(item)
    saveImageList(list, Constants.BOOKMARKED_LIST)
}

fun Context.removeImageFromList(item: SearchItem) : Boolean {
    val list = getImageList(Constants.BOOKMARKED_LIST)
    list.forEachIndexed { index, value ->
        if(item.imageUrl == value.imageUrl){
            list.removeAt(index)
            saveImageList(list, Constants.BOOKMARKED_LIST)
            return true
        }
    }
    return false
}

fun Context.isBookMarked(url: String) : Boolean {
    val list = getImageList(Constants.BOOKMARKED_LIST)
    list.forEach { value ->
        if(url == value.imageUrl)
            return true
    }
    return false
}

fun showToast(context: Context, msg : String) = Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
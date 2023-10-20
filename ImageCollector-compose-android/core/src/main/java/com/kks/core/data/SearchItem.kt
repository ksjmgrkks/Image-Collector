package com.kks.core.data

import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

data class SearchItem(
    val imageUrl : String,
    val datetime : String,
    /* 북마크만 var로 설정한 이유
      링크: https://medium.com/tech-takeaways/how-to-update-paginated-data-with-the-android-paging-3-library-ef2d8581e77f */
    var bookmarked : Boolean,
)

val SearchItem.datetimeText: String get() = dateTimeFormatter(datetime)

fun dateTimeFormatter(dateTimeString: String) : String {
    val outputFormatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일\na hh시 mm분 ss초")
    return outputFormatter.format(OffsetDateTime.from(DateTimeFormatter.ISO_OFFSET_DATE_TIME.parse(dateTimeString)))
}
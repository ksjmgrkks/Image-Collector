package com.kakaobank.data.search.remote.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@JsonClass(generateAdapter = true)
data class ApiImagesResponse(
    @field:Json(name = "collection") val collection: String?, //컬렉션
    @field:Json(name = "thumbnail_url") val thumbnail_url: String?, //미리보기 이미지 URL
    @field:Json(name = "image_url") val image_url: String?, //이미지 URL
    @field:Json(name = "width") val width: Int?, //이미지의 가로 길이
    @field:Json(name = "height") val height: Int?, //이미지의 세로 길이
    @field:Json(name = "display_sitename") val display_sitename: String?, //출처
    @field:Json(name = "doc_url") val doc_url: String?, //문서 URL
    @field:Json(name = "datetime") val datetime: String, //문서 작성시간, ISO 8601 [YYYY]-[MM]-[DD]T[hh]:[mm]:[ss].000+[tz]
)


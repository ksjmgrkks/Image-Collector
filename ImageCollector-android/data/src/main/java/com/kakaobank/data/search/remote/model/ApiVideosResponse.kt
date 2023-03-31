package com.kakaobank.data.search.remote.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiVideosResponse(
    @field:Json(name = "title") val title: String?, //동영상 제목
    @field:Json(name = "url") val url: String?, //동영상 링크
    @field:Json(name = "datetime") val datetime: String?, //동영상 등록일, ISO 8601 [YYYY]-[MM]-[DD]T[hh]:[mm]:[ss].000+[tz]
    @field:Json(name = "play_time") val play_time: Int?, //동영상 재생시간, 초 단위
    @field:Json(name = "thumbnail") val thumbnail: String?, //동영상 미리보기 URL
    @field:Json(name = "author") val author: String?, //동영상 업로더
)
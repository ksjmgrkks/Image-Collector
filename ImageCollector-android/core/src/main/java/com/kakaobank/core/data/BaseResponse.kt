package com.kakaobank.core.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BaseResponse<T>(
    @field:Json(name = "meta") val meta: Meta,
    @field:Json(name = "documents") val documents: List<T>
) {
    @JsonClass(generateAdapter = true)
    data class Meta(
        @field:Json(name = "total_count") val totalCount: Int = 0, // 검색된 문서 수
        @field:Json(name = "pageable_count") val pageableCount: Int = 0, // total_count 중 노출 가능 문서 수
        @field:Json(name = "is_end") val isEnd: Boolean = true, // 현재 페이지가 마지막 페이지인지 여부, 값이 false면 page를 증가시켜 다음 페이지를 요청할 수 있음
    )
}

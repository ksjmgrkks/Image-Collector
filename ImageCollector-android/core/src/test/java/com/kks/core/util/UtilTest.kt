package com.kks.core.util

import android.content.Context
import android.content.SharedPreferences
import com.kks.core.data.SearchItem
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class UtilTest {

    @Mock
    lateinit var mockContext: Context

    @Mock
    lateinit var mockSharedPreferences: SharedPreferences

    @Mock
    lateinit var mockEditor: SharedPreferences.Editor

    private lateinit var adapter: JsonAdapter<List<SearchItem>>

    private lateinit var searchItemList: ArrayList<SearchItem>

    @Before
    fun setup() {
        `when`(mockContext.getSharedPreferences(Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE)).thenReturn(mockSharedPreferences)
        `when`(mockSharedPreferences.edit()).thenReturn(mockEditor)

        adapter = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
            .adapter(Types.newParameterizedType(List::class.java, SearchItem::class.java))

        searchItemList = arrayListOf(
            SearchItem("https://search2.kakaocdn.net/argon/130x130_85_c/GeaCzXlWGBl", "2023-03-23T08:30:22.000+09:00", true),
            SearchItem("https://search4.kakaocdn.net/argon/130x130_85_c/DX99LNwopf4","2023-03-23T08:27:00.000+09:00", true),
            SearchItem("https://search1.kakaocdn.net/argon/130x130_85_c/5xUaLDDE2rR","2023-03-23T08:27:00.000+09:00", false))
    }

    @Test
    fun `saveImageList_SearchItem 값이 있는 ArrayList를 저장했을 때_mockEditor의 putString과 apply 함수가 실행됨`(){
        //Given
        /* setup() searchItemList */

        //When
        mockContext.saveImageList(searchItemList, Constants.BOOKMARKED_LIST)

        //Then
        verify(mockEditor).putString(Constants.BOOKMARKED_LIST, adapter.toJson(searchItemList))
        verify(mockEditor).apply()
    }

    @Test
    fun `saveImageList_SearchItem 값이 없는 ArrayList를 저장했을 때_mockEditor의 putString과 apply 함수가 실행됨`(){
        //Given
        val fakeList : ArrayList<SearchItem> = arrayListOf()

        //When
        mockContext.saveImageList(fakeList, Constants.BOOKMARKED_LIST)

        //Then
        verify(mockEditor).putString(Constants.BOOKMARKED_LIST, adapter.toJson(fakeList))
        verify(mockEditor).apply()
    }

    @Test
    fun `getImageList_setup()에서 설정한 searchItemList를 getImageList를 통해 불러옴_searchItemList 값과 불러온 값이 일치함`() {
        //Given
        `when`(mockSharedPreferences.getString(Constants.BOOKMARKED_LIST, null)).thenReturn(adapter.toJson(searchItemList))

        //When
        val list = mockContext.getImageList(Constants.BOOKMARKED_LIST)

        //Then
        assertEquals(3, list.size)
        assertEquals("https://search2.kakaocdn.net/argon/130x130_85_c/GeaCzXlWGBl", list[0].imageUrl)
        assertEquals("https://search4.kakaocdn.net/argon/130x130_85_c/DX99LNwopf4", list[1].imageUrl)
        assertEquals("https://search1.kakaocdn.net/argon/130x130_85_c/5xUaLDDE2rR", list[2].imageUrl)
    }

    @Test
    fun `getImageList_sharedPreferences에 저장된 값이 없을 때 getImageList를 통해 불러옴_빈 리스트를 리턴함`() {
        //Given
        `when`(mockSharedPreferences.getString(Constants.BOOKMARKED_LIST, null)).thenReturn(null)

        //When
        val list = mockContext.getImageList(Constants.BOOKMARKED_LIST)

        //Then
        assertEquals(0, list.size)
    }

    @Test
    fun `addImageToList_searchItemList의 첫번째 값을 가지고 해당 함수를 실행하면_내부 saveImageList가 호출됨`() {
        //Given
        `when`(mockSharedPreferences.getString(Constants.BOOKMARKED_LIST, null)).thenReturn(adapter.toJson(searchItemList))
        val item = searchItemList[0]
        val copyList = searchItemList
        copyList.add(item)

        //When
        mockContext.addImageToList(item)

        //Then
        verify(mockEditor).putString(Constants.BOOKMARKED_LIST, adapter.toJson(copyList)) /* item을 추가한 copyList와 같은 형태로 저장됨*/
        verify(mockEditor).apply()
    }

    @Test
    fun `removeImageFromList_searchItemList의 첫번째 값을 가지고 해당 함수를 실행하면_삭제되어 true를 리턴함`() {
        //Given
        `when`(mockSharedPreferences.getString(Constants.BOOKMARKED_LIST, null)).thenReturn(adapter.toJson(searchItemList))
        val item = searchItemList[0]

        //When
        val removed = mockContext.removeImageFromList(item)

        //Then
        assertTrue(removed)
    }

    @Test
    fun `removeImageFromList_저장되지 않은 SearchItem을 가지고 해당 함수를 실행하면_삭제되지 않아 false를 리턴함`() {
        //Given
        `when`(mockSharedPreferences.getString(Constants.BOOKMARKED_LIST, null)).thenReturn(adapter.toJson(searchItemList))
        val item = SearchItem("testUrl", "2023-03-23T08:30:22.000+09:00", false)

        //When
        val removed = mockContext.removeImageFromList(item)

        //Then
        assertFalse(removed)
    }

    @Test
    fun `isBookMarked_setup()에서 설정한 searchItemList에 있는 Url 값과 없는 Url 값을 각각 호출함_case1은 true, case2는 false를 리턴함`() {
        //Given
        `when`(mockSharedPreferences.getString(Constants.BOOKMARKED_LIST, null)).thenReturn(adapter.toJson(searchItemList))

        //When
        val case1 = mockContext.isBookMarked("https://search2.kakaocdn.net/argon/130x130_85_c/GeaCzXlWGBl")
        val case2 = mockContext.isBookMarked("testUrl")

        //Then
        assertTrue(case1)
        assertFalse(case2)
    }
}
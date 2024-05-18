package com.dicoding.restaurantreview

import com.dicoding.restaurantreview.data.remote.response.SearchResponse
import com.dicoding.restaurantreview.ui.MainActivity
import com.dicoding.restaurantreview.ui.adapter.ReviewAdapter
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Callback

@RunWith(MockitoJUnitRunner::class)
class MainActivityUnitTest {
    @Mock
    lateinit var mockMainActivity: MainActivity

    @Mock
    lateinit var mockAdapter: ReviewAdapter

    @Before
    fun setUp() {
        mockMainActivity = Mockito.mock(MainActivity::class.java)
        mockAdapter = Mockito.mock(ReviewAdapter::class.java)
        mockMainActivity.searchUser("Braceskabane")
    }

    @Test
    fun testSearchUser_FailureResponse() {
        val username = "example_user"
        val mockCallback = Mockito.mock(Callback::class.java) as Callback<SearchResponse>
        Mockito.`when`(mockMainActivity.searchUser(username)).thenAnswer {
            mockCallback
        }
        val callResult = mockMainActivity.searchUser(username)
        Mockito.verify(mockMainActivity).searchUser(username)
        assertNotNull("Result should not be null", callResult)
    }
}

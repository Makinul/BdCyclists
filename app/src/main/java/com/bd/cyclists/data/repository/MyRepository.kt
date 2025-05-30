package com.bd.cyclists.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.bd.cyclists.data.model.MovieItem
import com.bd.cyclists.data.model.Post
import com.bd.cyclists.data.service.ApiService
import com.bd.cyclists.utils.MoviePagingSource
import kotlinx.coroutines.flow.Flow

// In MyRepository (updated)
class MyRepository(
    private val apiService: ApiService // Inject your Ktor API service
) {

    fun nowPlayingMovies(page: Int): Flow<PagingData<MovieItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = page
            ),
            pagingSourceFactory = { MoviePagingSource(apiService) }
        ).flow

//        Flow<PagingData<MovieItem>> = Pager(
//            config = PagingConfig(
//                pageSize = 20,          // Number of items per page
//                enablePlaceholders = false, // Set to true if you want placeholders for un-loaded items
//                initialLoadSize = 20    // Initial number of items to load
//            ),
//            pagingSourceFactory = { MoviePagingSource(myRepository) }
//        ).flow.cachedIn(viewModelScope) // Cache the PagingData flow within the ViewModel's scope
//        return apiService.nowPlayingMovies(page)
    }

    suspend fun getUserPosts(): List<Post> {
        return apiService.getPosts()
    }

    suspend fun fetchSinglePost(postId: Int): Post {
        return apiService.getPostById(postId)
    }
}
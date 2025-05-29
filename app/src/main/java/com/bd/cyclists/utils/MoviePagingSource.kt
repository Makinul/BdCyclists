package com.bd.cyclists.utils

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.bd.cyclists.data.model.MovieItem
import com.bd.cyclists.data.service.ApiService
import kotlinx.io.IOException

class MoviePagingSource(private val apiService: ApiService) :
    PagingSource<Int, MovieItem>() {
    override fun getRefreshKey(state: PagingState<Int, MovieItem>): Int? {
        // This is used for refreshing the list (e.g., pull-to-refresh)
        // It returns the most recently accessed page key.
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieItem> {
        val currentPage = params.key ?: 1 // Start from page 1, or use provided key

        return try {
            val response = apiService.nowPlayingMovies(currentPage)
            val list = response.results
            LoadResult.Page(
                data = list,
                prevKey = if (currentPage == 1) null else currentPage - 1, // Null if first page
                nextKey = if (response.page >= response.totalPages) null else currentPage + 1 // Null if no more data
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        }
    }
}
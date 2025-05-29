package com.bd.cyclists

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bd.cyclists.data.model.Post
import com.bd.cyclists.data.repository.MyRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.bd.cyclists.data.model.BaseModel
import com.bd.cyclists.data.model.MovieItem
import com.bd.cyclists.data.service.ApiService
import com.bd.cyclists.utils.MoviePagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Example: Represents some data your UI might display
// Example: Represents some data your UI might display
data class MyUiState(
    val message: String = "Initial Message",
    val isLoading: Boolean = false,
    val posts: List<Post> = emptyList(), // To hold data from the repository
    val error: String? = null
)

class MainViewModel(
    private val myRepository: MyRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MyUiState())
    val uiState: StateFlow<MyUiState> = _uiState

    init {
        loadPosts()
    }

    fun loadPosts() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val posts = myRepository.getUserPosts()
                delay(2000)
                _uiState.value =
                    _uiState.value.copy(isLoading = false, posts = posts, message = "Posts Loaded!")
            } catch (e: Exception) {
                print(e.message)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Failed to load posts: ${e.localizedMessage}"
                )
            }
        }
    }

    // Add more functions here to handle UI events and update state
    fun updateMessage(newMessage: String) {
        _uiState.value = _uiState.value.copy(message = newMessage)
    }

    // You can add other functions that use the repository
    fun fetchSpecificPost(postId: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val post = myRepository.fetchSinglePost(postId)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    message = "Fetched post: ${post.title}",
                    // You might want to update a specific post list or a single post state
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Failed to fetch post: ${e.localizedMessage}"
                )
            }
        }
    }

    fun loadMovieList() {
        viewModelScope.launch {

        }
    }

//    val moviePagingSource: Flow<PagingData<MovieItem>> = Pager(
//        config = PagingConfig(
//            pageSize = 20,          // Number of items per page
//            enablePlaceholders = false, // Set to true if you want placeholders for un-loaded items
//            initialLoadSize = 20    // Initial number of items to load
//        ),
//        pagingSourceFactory = { MoviePagingSource(myRepository) }
//    ).flow.cachedIn(viewModelScope) // Cache the PagingData flow within the ViewModel's scope

    val moviePagingSource: Flow<PagingData<MovieItem>> =
        myRepository.nowPlayingMovies(1).cachedIn(viewModelScope)
}
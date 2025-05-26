package com.bd.cyclists

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bd.cyclists.data.model.Post
import com.bd.cyclists.data.repository.MyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

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
                val posts = myRepository.getUserPosts() // Call the repository
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
                _uiState.value = _uiState.value.copy(isLoading = false, error = "Failed to fetch post: ${e.localizedMessage}")
            }
        }
    }
}
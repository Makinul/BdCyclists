package com.bd.cyclist.data.repository

import com.bd.cyclist.data.model.Post
import com.bd.cyclist.data.service.ApiService

// In MyRepository (updated)
class MyRepository(
    private val apiService: ApiService // Inject your Ktor API service
) {
    suspend fun getUserPosts(): List<Post> {
        return apiService.getPosts() // Call API service
    }

    suspend fun fetchSinglePost(postId: Int): Post {
        return apiService.getPostById(postId)
    }
}
package com.bd.cyclists.data.service

import com.bd.cyclists.data.model.Post
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.HttpClient // Import HttpClient

interface ApiService {
    suspend fun getPosts(): List<Post>
    suspend fun getPostById(id: Int): Post
}

class ApiServiceImpl(private val httpClient: HttpClient) : ApiService {
    private val BASE_URL = "https://jsonplaceholder.typicode.com" // Example API

    override suspend fun getPosts(): List<Post> {
        return httpClient.get("$BASE_URL/posts").body()
    }

    override suspend fun getPostById(id: Int): Post {
        return httpClient.get("$BASE_URL/posts/$id").body()
    }
}
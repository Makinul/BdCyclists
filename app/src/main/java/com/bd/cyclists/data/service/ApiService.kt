package com.bd.cyclists.data.service

import com.bd.cyclists.data.model.BaseModel
import com.bd.cyclists.data.model.MovieItem
import com.bd.cyclists.data.model.Post
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.HttpClient // Import HttpClient
import io.ktor.http.encodedPath

interface ApiService {
    suspend fun nowPlayingMovies(
        page: Int
    ): BaseModel<MovieItem>

    suspend fun getPosts(): List<Post>
    suspend fun getPostById(id: Int): Post
}

class ApiServiceImpl(private val httpClient: HttpClient) : ApiService {

    override suspend fun nowPlayingMovies(
        page: Int
    ): BaseModel<MovieItem> {
        return httpClient.get {
            url {
                encodedPath = "movie/now_playing"
                parameters.append("page", page.toString())
            }
        }.body()
    }

    override suspend fun getPosts(): List<Post> {
        return httpClient.get("https://jsonplaceholder.typicode.com/posts").body()
    }

    override suspend fun getPostById(id: Int): Post {
        return httpClient.get("https://jsonplaceholder.typicode.com/posts/$id").body()
    }
}
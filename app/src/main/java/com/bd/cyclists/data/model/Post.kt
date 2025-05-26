package com.bd.cyclists.data.model

import kotlinx.serialization.Serializable

@Serializable // Mark your data class as serializable
data class Post(
    val id: Int,
    val userId: Int,
    val title: String,
    val body: String
)
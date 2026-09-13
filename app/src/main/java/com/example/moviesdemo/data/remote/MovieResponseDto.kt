package com.example.moviesdemo.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// Это тот самый DTO из документа: зеркалит JSON, ничего лишнего не решает
@Serializable
data class MovieResponseDto(
    val results: List<MovieDto>
)

@Serializable
data class MovieDto(
    val id: Int,
    val title: String,
    @SerialName("vote_average")
    val voteAverage: Double
)
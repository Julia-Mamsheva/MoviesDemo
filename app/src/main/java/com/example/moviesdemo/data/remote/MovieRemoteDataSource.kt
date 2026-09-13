package com.example.moviesdemo.data.remote

import com.example.moviesdemo.data.remote.dto.MovieDto
import javax.inject.Inject

// Отдельный класс — SRP: единственная забота, сходить в сеть и вернуть DTO
class MovieRemoteDataSource @Inject constructor(
    private val api: MovieApi
) {
    suspend fun fetchPopularMovies(): List<MovieDto> = api.getPopularMovies().results
}
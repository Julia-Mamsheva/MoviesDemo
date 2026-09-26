package com.example.moviesdemo.data.remote

import com.example.moviesdemo.data.remote.dto.MovieResponseDto
import retrofit2.http.GET

interface MovieApi {
    @GET("movie/popular")
    suspend fun getPopularMovies(): MovieResponseDto
}
package com.example.moviesdemo.data.local

import com.example.moviesdemo.domain.model.Movie
import kotlinx.coroutines.flow.first
import javax.inject.Inject


// Раньше DAO использовался напрямую в MovieRepositoryImpl — теперь,
// когда источников два, разделяем по SRP: каждый DataSource — одна забота
class MovieLocalDataSource @Inject constructor(
    private val dao: MovieDao
) {
    suspend fun getCachedMovies(): List<Movie> = dao.observeAll().first().map { it.toDomain() }
    suspend fun hasCachedMovies(): Boolean = dao.count() > 0
    suspend fun cacheMovies(movies: List<Movie>) = dao.insertAll(movies.map { it.toEntity() })
    suspend fun setFavorite(movieId: String, isFavorite: Boolean) = dao.setFavorite(movieId, isFavorite)
}
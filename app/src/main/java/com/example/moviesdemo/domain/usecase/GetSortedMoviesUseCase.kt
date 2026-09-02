package com.example.moviesdemo.domain.usecase

import com.example.moviesdemo.domain.model.Movie
import com.example.moviesdemo.domain.repository.MovieRepository


// UseCase = один бизнес-сценарий = один класс.
// Здесь есть логика (сортировка) — поэтому вынести
// её в отдельный класс оправдано
class GetSortedMoviesUseCase(
    private val repository: MovieRepository
) {
    // operator fun invoke() позволяет вызывать useCase(...) как функцию
    suspend operator fun invoke(sortBy: SortOrder): List<Movie> {
        val movies = repository.getMovies()
        return when (sortBy) {
            SortOrder.RATING -> movies.sortedByDescending { it.rating }
            SortOrder.TITLE -> movies.sortedBy { it.title }
        }
    }
}

enum class SortOrder { RATING, TITLE }
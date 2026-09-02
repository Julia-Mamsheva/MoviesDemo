package com.example.moviesdemo.domain.usecase

import com.example.moviesdemo.domain.model.Movie
import com.example.moviesdemo.domain.repository.MovieRepository
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Test


// Fake — простая ручная реализация интерфейса Domain
class FakeMovieRepository : MovieRepository {
    private var movies = listOf<Movie>()
    fun setMovies(list: List<Movie>) { movies = list }
    override suspend fun getMovies() = movies
    override suspend fun toggleFavorite(movieId: String) {}
}

class GetSortedMoviesUseCaseTest {
    private val fakeRepository = FakeMovieRepository()
    private val useCase = GetSortedMoviesUseCase(fakeRepository)

    @Test
    fun `сортирует фильмы по рейтингу по убыванию`() = runTest {
        fakeRepository.setMovies(listOf(
            Movie("1", "A", rating = 5.0, isFavorite = false),
            Movie("2", "B", rating = 9.0, isFavorite = false)
        ))

        val result = useCase(SortOrder.RATING)

        assertEquals("B", result.first().title)
    }
}
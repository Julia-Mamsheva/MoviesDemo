package com.example.moviesdemo.domain.usecase

import com.example.moviesdemo.domain.model.Movie
import com.example.moviesdemo.domain.repository.MovieRepository
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Test


class GetSortedMoviesUseCaseTest {

    private val fakeRepository = FakeMovieRepository()
    private val useCase = GetSortedMoviesUseCase(fakeRepository)

    private fun movie(id: String, title: String, rating: Double, fav: Boolean = false) =
        Movie(id, title, rating, fav)

    @Test
    fun `сортирует фильмы по рейтингу по убыванию`() = runTest {
        fakeRepository.setMovies(listOf(movie("1", "A", 5.0), movie("2", "B", 9.0)))

        val result = useCase(SortOrder.RATING)

        assertEquals("B", result.first().title)
    }

    @Test
    fun `сортирует по рейтингу полностью, а не только первый элемент`() = runTest {
        fakeRepository.setMovies(listOf(
            movie("1", "A", 5.0), movie("2", "B", 9.0), movie("3", "C", 7.0)
        ))

        val result = useCase(SortOrder.RATING)

        assertEquals(listOf("2", "3", "1"), result.map { it.id })
    }

    @Test
    fun `сортирует по названию по алфавиту`() = runTest { 
        fakeRepository.setMovies(listOf(
            movie("1", "Zorro", 5.0), movie("2", "Avatar", 9.0), movie("3", "Matrix", 7.0)
        ))

        val result = useCase(SortOrder.TITLE)

        assertEquals(listOf("Avatar", "Matrix", "Zorro"), result.map { it.title })
    }

    @Test
    fun `пустой список возвращает пустой список`() = runTest {
        fakeRepository.setMovies(emptyList())

        assertEquals(emptyList<Movie>(), useCase(SortOrder.RATING))
    }

    @Test
    fun `фильмы с одинаковым рейтингом не теряются`() = runTest {
        fakeRepository.setMovies(listOf(movie("1", "A", 8.0), movie("2", "B", 8.0)))

        val result = useCase(SortOrder.RATING)

        assertEquals(setOf("1", "2"), result.map { it.id }.toSet())
    }
}
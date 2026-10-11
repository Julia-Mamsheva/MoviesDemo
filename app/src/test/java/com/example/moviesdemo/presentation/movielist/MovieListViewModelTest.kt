package com.example.moviesdemo.presentation.movielist

import app.cash.turbine.test
import com.example.moviesdemo.MainDispatcherRule
import com.example.moviesdemo.domain.model.Movie
import com.example.moviesdemo.domain.usecase.FakeMovieRepository
import com.example.moviesdemo.domain.usecase.GetSortedMoviesUseCase
import com.example.moviesdemo.domain.usecase.SortOrder
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MovieListViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()  // Unconfined

    private lateinit var fakeRepository: FakeMovieRepository
    private lateinit var viewModel: MovieListViewModel

    private fun createViewModel() {
        viewModel = MovieListViewModel(GetSortedMoviesUseCase(fakeRepository), fakeRepository)
    }

    @Before
    fun setUp() {
        fakeRepository = FakeMovieRepository()
        // Данные кладём ДО создания ViewModel: она грузит их в init
        fakeRepository.setMovies(listOf(
            Movie("1", "A", 5.0, false),
            Movie("2", "B", 9.0, false),
        ))
        createViewModel()
    }

    @Test
    fun `после загрузки isLoading становится false`() = runTest {
        viewModel.uiState.test {
            val state = awaitItem()
            assertFalse(state.isLoading)
            assertEquals(2, state.movies.size)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `фильмы приходят отсортированными по умолчанию`() = runTest {
        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals("B", state.movies.first().title)   // ⚠️ зависит от сортировки по умолчанию
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `toggleFavorite помечает фильм избранным`() = runTest {
        viewModel.onFavoriteClicked("1")

        viewModel.uiState.test {
            val movie = awaitItem().movies.first { it.id == "1" }
            assertTrue(movie.isFavorite)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `повторный toggleFavorite снимает избранное`() = runTest {
        viewModel.onFavoriteClicked("1")
        viewModel.onFavoriteClicked("1")

        viewModel.uiState.test {
            assertFalse(awaitItem().movies.first { it.id == "1" }.isFavorite)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `смена сортировки меняет порядок фильмов`() = runTest {
        viewModel.onSortOrderChanged(SortOrder.TITLE)

        viewModel.uiState.test {
            assertEquals("A", awaitItem().movies.first().title)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `при ошибке загрузки isLoading false и есть сообщение об ошибке`() = runTest {
        fakeRepository.shouldThrow = true
        createViewModel()

        viewModel.uiState.test {
            val state = awaitItem()
            assertFalse(state.isLoading)
            assertNotNull(state.errorMessage)
            cancelAndIgnoreRemainingEvents()
        }
    }
}
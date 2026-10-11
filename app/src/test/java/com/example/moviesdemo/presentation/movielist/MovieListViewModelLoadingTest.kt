package com.example.moviesdemo.presentation.movielist

import app.cash.turbine.test
import com.example.moviesdemo.MainDispatcherRule
import com.example.moviesdemo.domain.model.Movie
import com.example.moviesdemo.domain.usecase.FakeMovieRepository
import com.example.moviesdemo.domain.usecase.GetSortedMoviesUseCase
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue

class MovieListViewModelLoadingTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(StandardTestDispatcher())

    @Test
    fun `isLoading true до загрузки и false после`() = runTest {
        val fake = FakeMovieRepository().apply { setMovies(listOf(Movie("1", "A", 5.0, false))) }
        val viewModel = MovieListViewModel(GetSortedMoviesUseCase(fake), fake)

        viewModel.uiState.test {
            assertTrue(awaitItem().isLoading)    // корутина init ещё в очереди, не выполнена
            advanceUntilIdle()                   // выполняем всё, что стоит в очереди
            val loaded = awaitItem()
            assertFalse(loaded.isLoading)
            assertEquals(1, loaded.movies.size)
            cancelAndIgnoreRemainingEvents()
        }
    }
}
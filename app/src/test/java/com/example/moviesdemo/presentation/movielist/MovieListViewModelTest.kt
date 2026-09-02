package com.example.moviesdemo.presentation.movielist

import app.cash.turbine.test
import com.example.moviesdemo.MainDispatcherRule
import com.example.moviesdemo.domain.model.Movie
import com.example.moviesdemo.domain.usecase.FakeMovieRepository
import com.example.moviesdemo.domain.usecase.GetSortedMoviesUseCase
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MovieListViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var fakeRepository: FakeMovieRepository
    private lateinit var viewModel: MovieListViewModel

    @Before
    fun setUp() {
        fakeRepository = FakeMovieRepository()
        fakeRepository.setMovies(listOf(Movie("1", "A", 5.0, false))) // ← до создания ViewModel
        val useCase = GetSortedMoviesUseCase(fakeRepository)
        viewModel = MovieListViewModel(useCase, fakeRepository)
    }

    @Test
    fun `после загрузки isLoading становится false`() = runTest {
        viewModel.uiState.test {
            val state = awaitItem()
            assertFalse(state.isLoading)
            assertEquals(1, state.movies.size)
            cancelAndIgnoreRemainingEvents()
        }
    }
}
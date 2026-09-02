package com.example.moviesdemo.presentation.movielist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviesdemo.domain.repository.MovieRepository
import com.example.moviesdemo.domain.usecase.GetSortedMoviesUseCase
import com.example.moviesdemo.domain.usecase.SortOrder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject



@HiltViewModel
class MovieListViewModel @Inject constructor(
    private val getSortedMovies: GetSortedMoviesUseCase,
    private val repository: MovieRepository // для toggleFavorite
) : ViewModel() {

    private val _uiState = MutableStateFlow(MovieListUiState())
    val uiState: StateFlow<MovieListUiState> = _uiState.asStateFlow()

    init {
        loadMovies() // грузим сразу при создании ViewModel
    }

    fun onSortOrderChanged(order: SortOrder) {
        _uiState.update { it.copy(sortOrder = order) }
        loadMovies() // пересортировать с новым параметром
    }

    fun onFavoriteClicked(movieId: String) = viewModelScope.launch {
        repository.toggleFavorite(movieId)
        loadMovies() // обновить список после изменения
    }

    private fun loadMovies() = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true) }
        val movies = getSortedMovies(_uiState.value.sortOrder)
        _uiState.update { it.copy(isLoading = false, movies = movies) }
    }
}
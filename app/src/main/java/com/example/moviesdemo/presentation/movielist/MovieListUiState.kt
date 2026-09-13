package com.example.moviesdemo.presentation.movielist

import com.example.moviesdemo.domain.model.Movie
import com.example.moviesdemo.domain.usecase.SortOrder

// Состояние ЭКРАНА, а не бизнес-модель. Знает про загрузку и ошибки —
// доменная Movie про такое знать не должна.
data class MovieListUiState(
    val movies: List<Movie> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null, //при подключении сети может появяится ошибка
    val sortOrder: SortOrder = SortOrder.RATING
)
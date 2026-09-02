package com.example.moviesdemo.presentation.movielist

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.moviesdemo.domain.model.Movie
import com.example.moviesdemo.domain.usecase.SortOrder
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder


// "Умная" версия — знает про ViewModel
@Composable
fun MovieListScreen(viewModel: MovieListViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    MovieListContent(
        uiState = uiState,
        onFavoriteClick = viewModel::onFavoriteClicked,
        onSortChange = viewModel::onSortOrderChanged
    )
}

// "Глупая" версия — state hoisting. Не знает про ViewModel вообще,
// поэтому её можно тестировать и превьюшить без реального ViewModel.
@Composable
fun MovieListContent(
    uiState: MovieListUiState,
    onFavoriteClick: (String) -> Unit,
    onSortChange: (SortOrder) -> Unit
) {
    Column {
        Row {
            SortOrder.entries.forEach { order ->
                TextButton(onClick = { onSortChange(order) }) {
                    Text(order.name)
                }
            }
        }

        if (uiState.isLoading) {
            CircularProgressIndicator(modifier = Modifier.testTag("loading"))
        } else {
            LazyColumn {
                items(uiState.movies, key = { it.id }) { movie ->
                    MovieRow(
                        movie = movie,
                        onFavoriteClick = { onFavoriteClick(movie.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun MovieRow(movie: Movie, onFavoriteClick: () -> Unit) {
    Row(modifier = Modifier.testTag("movie_row_${movie.id}")) {
        Log.d("MovieRow", "Recompose: ${movie.title} isFavorite=${movie.isFavorite}")
        Text(movie.title, modifier = Modifier.weight(1f))
        Text("${movie.rating} ★")
        IconButton(
            onClick = onFavoriteClick,
            modifier = Modifier.testTag("favorite_button_${movie.id}")
        ) {
            Icon(
                imageVector = if (movie.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = "В избранное"
            )
        }
    }
}
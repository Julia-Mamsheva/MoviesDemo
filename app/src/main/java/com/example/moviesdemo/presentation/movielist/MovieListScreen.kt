package com.example.moviesdemo.presentation.movielist

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
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
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.moviesdemo.R
import java.util.jar.Manifest


// "Умная" версия — знает про ViewModel
@Composable
fun MovieListScreen(viewModel: MovieListViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    var pendingMovieId by remember { mutableStateOf<String?>(null) }
    var pendingCameraUri by remember { mutableStateOf<Uri?>(null) }

    // Галерея — без разрешений, современный Photo Picker
    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        val movieId = pendingMovieId
        if (uri != null && movieId != null) {
            viewModel.onCoverImageSelected(movieId, uri.toString())
        }
    }

    // Камера — результат в заранее подготовленный Uri
    val takePhotoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        val movieId = pendingMovieId
        val uri = pendingCameraUri
        if (success && movieId != null && uri != null) {
            viewModel.onCoverImageSelected(movieId, uri.toString())
        }
    }
    // Разрешение на камеру — запрашивается перед запуском TakePicture
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            val uri = createCameraOutputUri(context)
            pendingCameraUri = uri
            takePhotoLauncher.launch(uri)
        }
    }
    MovieListContent(
        uiState = uiState,
        onFavoriteClick = viewModel::onFavoriteClicked,
        onSortChange = viewModel::onSortOrderChanged,
        onGalleryClick = { movieId ->
            pendingMovieId = movieId
            pickImageLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        },
        onCameraClick = { movieId ->
            pendingMovieId = movieId
            cameraPermissionLauncher.launch(android.Manifest.permission.CAMERA)
        }
    )
}

// "Глупая" версия — state hoisting. Не знает про ViewModel вообще,
// поэтому её можно тестировать и превьюшить без реального ViewModel.
@Composable
fun MovieListContent(
    uiState: MovieListUiState,
    onFavoriteClick: (String) -> Unit,
    onSortChange: (SortOrder) -> Unit,
    onGalleryClick: (String) -> Unit,
    onCameraClick: (String) -> Unit
) {
    Column {
        Row {
            SortOrder.entries.forEach { order ->
                TextButton(onClick = { onSortChange(order) }) {
                    Text(order.name)
                }
            }
        }

        if (uiState.errorMessage != null) {
            Text(uiState.errorMessage, color = MaterialTheme.colorScheme.error)
        } else if (uiState.isLoading) {
            CircularProgressIndicator(modifier = Modifier.testTag("loading_indicator"))
        }
        else {
            LazyColumn {
                items(uiState.movies, key = { it.id }) { movie ->
                    MovieRow(
                        movie = movie,
                        onFavoriteClick = { onFavoriteClick(movie.id) },
                        onGalleryClick = { onGalleryClick(movie.id) },
                        onCameraClick = { onCameraClick(movie.id) }
                    )
                }
            }
        }
    }
}

/**
 *
 * @param movie
 */
@Composable
fun MovieRow(
    movie: Movie,
    onFavoriteClick: () -> Unit,
    onGalleryClick: () -> Unit,
    onCameraClick: () -> Unit
) {
    Row(modifier = Modifier.testTag("movie_row_${movie.id}")) {
        AsyncImage(
            model = movie.coverImagePath ?: R.drawable.ic_no_cover, // заглушка, если обложки нет
            contentDescription = "Обложка фильма",
            modifier = Modifier.size(56.dp),
            contentScale = ContentScale.Crop
        )
        Text(movie.title, modifier = Modifier.weight(1f).testTag(movie.title))
        Text("${movie.rating} ★")

        IconButton(onClick = onGalleryClick, modifier = Modifier.testTag("gallery_button_${movie.id}")) {
            Icon(painterResource( R.drawable.ic_files), contentDescription = "Выбрать из галереи")
        }
        IconButton(onClick = onCameraClick, modifier = Modifier.testTag("camera_button_${movie.id}")) {
            Icon(painterResource(R.drawable.ic_camera), contentDescription = "Сделать фото")
        }
        IconButton(onClick = onFavoriteClick, modifier = Modifier.testTag("favorite_button_${movie.id}")) {
            Icon(
                imageVector = if (movie.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = "В избранное"
            )
        }
    }
}
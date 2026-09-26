package com.example.moviesdemo.domain.repository

import com.example.moviesdemo.domain.model.Movie

// Интерфейс — контракт. Domain говорит "мне нужны такие операции",
// но НЕ знает, откуда данные придут (сеть? база? память?)
// Это и есть DIP: Domain объявляет абстракцию, Data её реализует
interface MovieRepository {
    suspend fun getMovies(): List<Movie>
    suspend fun toggleFavorite(movieId: String)
    suspend fun updateCoverImage(movieId: String, sourceUri: String): String
    // sourceUri — это Uri.toString() из галереи/камеры.
    // Возвращает итоговый ЛОКАЛЬНЫЙ путь, куда картинка реально сохранена
}
package com.example.moviesdemo.domain.repository

import com.example.moviesdemo.domain.model.Movie

// Интерфейс — контракт. Domain говорит "мне нужны такие операции",
// но НЕ знает, откуда данные придут (сеть? база? память?)
// Это и есть DIP: Domain объявляет абстракцию, Data её реализует
interface MovieRepository {
    suspend fun getMovies(): List<Movie>
    suspend fun toggleFavorite(movieId: String)
}
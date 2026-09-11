package com.example.moviesdemo.data.local

import androidx.room3.Entity
import androidx.room3.PrimaryKey

// Entity описывает СТРОКУ ТАБЛИЦЫ — формат хранения, а не бизнес-понятие.
// Это прямой аналог DTO из документа про Clean Architecture, только для базы, а не для сети.
@Entity(tableName = "movies")
data class MovieEntity(
    @PrimaryKey val id: String,
    val title: String,
    val rating: Double,
    val isFavorite: Boolean
)
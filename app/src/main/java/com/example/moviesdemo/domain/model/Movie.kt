package com.example.moviesdemo.domain.model

// Доменная модель — то, как приложение понимает фильм
// Никаких @SerializedName, никаких Android-аннотаций — чистый Kotlin
data class Movie(
    val id: String,
    val title: String,
    val rating: Double,
    val isFavorite: Boolean
)
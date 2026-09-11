package com.example.moviesdemo.data.local

import com.example.moviesdemo.domain.model.Movie


// Тот же принцип, что и DTO → Domain маппер: Domain не должен знать про @Entity/@PrimaryKey
fun MovieEntity.toDomain() = Movie(
    id = id,
    title = title,
    rating = rating,
    isFavorite = isFavorite
)

fun Movie.toEntity() = MovieEntity(
    id = id,
    title = title,
    rating = rating,
    isFavorite = isFavorite
)
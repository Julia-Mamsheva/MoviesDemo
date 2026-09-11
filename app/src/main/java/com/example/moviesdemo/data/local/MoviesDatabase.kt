package com.example.moviesdemo.data.local

import androidx.room3.Database
import androidx.room3.RoomDatabase


@Database(entities = [MovieEntity::class], version = 1)
abstract class MoviesDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
}
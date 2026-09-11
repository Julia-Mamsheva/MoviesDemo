package com.example.moviesdemo.data.local

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface MovieDao {
    // Flow — Room сам будет эмитить новый список при любом изменении таблицы,
    // подписчику не нужно вручную перезапрашивать данные
    @Query("SELECT * FROM movies")
    fun observeAll(): Flow<List<MovieEntity>>

    @Query("SELECT COUNT(*) FROM movies")
    suspend fun count(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(movies: List<MovieEntity>)

    @Query("UPDATE movies SET isFavorite = :isFavorite WHERE id = :movieId")
    suspend fun setFavorite(movieId: String, isFavorite: Boolean)
}
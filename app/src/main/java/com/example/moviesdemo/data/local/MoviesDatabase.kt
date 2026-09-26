package com.example.moviesdemo.data.local

import androidx.room3.Database
import androidx.room3.RoomDatabase
import androidx.room3.migration.Migration
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.sqlite.execSQL


@Database(entities = [MovieEntity::class], version = 2)
abstract class MoviesDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao


}
val MIGRATION_1_2 = object : Migration(1, 2) {

    override suspend fun migrate(db: SQLiteConnection) {
        db.execSQL("ALTER TABLE movies ADD COLUMN coverImagePath TEXT")
    }
}
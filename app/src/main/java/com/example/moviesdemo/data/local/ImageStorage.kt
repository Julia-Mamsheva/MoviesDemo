package com.example.moviesdemo.data.local

import android.content.Context
import androidx.core.net.toUri
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

// Интерфейс — та же причина, что и раньше: чтобы MovieRepositoryImpl
// можно было тестировать с фейком, не трогая реальную файловую систему
interface ImageStorage {
    suspend fun saveImage(sourceUri: String, movieId: String): String
}

class AndroidImageStorage @Inject constructor(
    @ApplicationContext private val context: Context
) : ImageStorage {

    override suspend fun saveImage(sourceUri: String, movieId: String): String =
        withContext(Dispatchers.IO) {
            val uri = sourceUri.toUri()
            val coversDir = File(context.filesDir, "covers").apply { mkdirs() }
            val destFile = File(coversDir, "$movieId.jpg")

            context.contentResolver.openInputStream(uri)?.use { input ->
                destFile.outputStream().use { output -> input.copyTo(output) }
            } ?: error("Не удалось открыть выбранное изображение")

            destFile.absolutePath // ← этот путь и сохраняем в Room
        }
}
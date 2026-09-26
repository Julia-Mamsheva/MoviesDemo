package com.example.moviesdemo.presentation.movielist

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File

// presentation/movielist/CameraUriProvider.kt

fun createCameraOutputUri(context: Context): Uri {
    val imagesDir = File(context.cacheDir, "images").apply { mkdirs() }
    val photoFile = File(imagesDir, "camera_${System.currentTimeMillis()}.jpg")
    return FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", photoFile)
}
package com.example.moviesdemo

import android.app.Application
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp // без этого Hilt не запустится вообще
class MoviesApplication : Application()
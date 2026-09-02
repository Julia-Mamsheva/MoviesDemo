package com.example.moviesdemo.movielist


import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.example.moviesdemo.domain.model.Movie
import com.example.moviesdemo.presentation.movielist.MovieListContent
import com.example.moviesdemo.presentation.movielist.MovieListUiState
import junit.framework.TestCase.assertEquals
import org.junit.Rule
import org.junit.Test


class MovieListScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `клик по избранному вызывает колбэк с id`() {
        var clickedId: String? = null

        composeTestRule.setContent {
            MovieListContent(
                uiState = MovieListUiState(
                    movies = listOf(Movie("42", "Фильм", 7.0, false))
                ),
                onFavoriteClick = { id -> clickedId = id },
                onSortChange = {}
            )
        }

        composeTestRule.onNodeWithTag("favorite_button_42").performClick()

        assertEquals("42", clickedId)
    }
}
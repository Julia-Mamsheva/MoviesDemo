package com.example.moviesdemo.movielist


import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.moviesdemo.domain.model.Movie
import com.example.moviesdemo.domain.usecase.SortOrder
import com.example.moviesdemo.presentation.movielist.MovieListContent
import com.example.moviesdemo.presentation.movielist.MovieListUiState
import junit.framework.TestCase.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test


class MovieListScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val movie = Movie("42", "Фильм", 7.0, false)

    private fun setContent(
        state: MovieListUiState = MovieListUiState(movies = listOf(movie), isLoading = false),
        onFavoriteClick: (String) -> Unit = {},
        onSortChange: (SortOrder) -> Unit = {},
        onGalleryClick: (String) -> Unit = {},
        onCameraClick: (String) -> Unit = {},
    ) {
        composeTestRule.setContent {
            MovieListContent(
                uiState = state,
                onFavoriteClick = onFavoriteClick,
                onSortChange = onSortChange,
                onGalleryClick = onGalleryClick,
                onCameraClick = onCameraClick,
            )
        }
    }

    @Test
    fun `клик по избранному вызывает колбэк с id`() {
        var clickedId: String? = null
        setContent(onFavoriteClick = { clickedId = it })

        composeTestRule.onNodeWithTag("favorite_button_42").performClick()

        assertEquals("42", clickedId)
    }

    @Test
    fun `название фильма отображается в списке`() {
        setContent()

        composeTestRule.onNodeWithText("Фильм").assertIsDisplayed()
    }

    @Test
    fun `при загрузке список не показывается`() {
        setContent(MovieListUiState(movies = emptyList(), isLoading = true))

        composeTestRule.onNodeWithTag("loading_indicator").assertIsDisplayed()
        composeTestRule.onNodeWithText("Фильм").assertDoesNotExist()
    }

    @Test
    fun `при ошибке показывается сообщение`() {
        setContent(MovieListUiState(isLoading = false, errorMessage = "Не удалось загрузить фильмы"))

        composeTestRule.onNodeWithText("Не удалось загрузить фильмы").assertIsDisplayed()
    }

    @Test
    fun `каждая кнопка сортировки вызывает колбэк со своим значением`() {
        val selected = mutableListOf<SortOrder>()
        setContent(onSortChange = { selected.add(it) })

        SortOrder.entries.forEach { order ->
            composeTestRule.onNodeWithText(order.name).performClick()
        }

        assertEquals(SortOrder.entries.toList(), selected)
    }

    @Test
    fun `клик по галерее вызывает колбэк`() {
        var called = false
        setContent(onGalleryClick = { called = true })

        composeTestRule.onNodeWithTag("gallery_button_42").performClick()

        assertTrue(called)
    }

    @Test
    fun `клик по камере вызывает колбэк`() {
        var called = false
        setContent(onCameraClick = { called = true })

        composeTestRule.onNodeWithTag("camera_button_42").performClick()

        assertTrue(called)
    }
}
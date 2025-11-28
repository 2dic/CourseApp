package com.example.features.favorites

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Course
import com.example.domain.repository.CourseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val repository: CourseRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FavoritesUiState())
    val uiState: StateFlow<FavoritesUiState> = _uiState.asStateFlow()

    private var originalFavorites: List<Course> = emptyList()

    init {
        loadFavorites()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadFavorites() {
        viewModelScope.launch {
            repository.getFavoriteCourses().collect { favorites ->
                originalFavorites = favorites
                val sortedFavorites = when (_uiState.value.sortType) {
                    SortType.BY_DATE -> favorites.sortedByDescending { it.publishDate.toLocalDate() }
                    SortType.BY_RATING -> favorites.sortedByDescending { it.rate.toFloatOrNull() ?: 0f }
                    SortType.BY_PRICE -> favorites.sortedByDescending {
                        it.price.replace(" ", "").toIntOrNull() ?: 0
                    }
                    SortType.NONE -> favorites
                }

                _uiState.value = _uiState.value.copy(
                    favoriteCourses = sortedFavorites,
                    isEmpty = favorites.isEmpty()
                )
            }
        }
    }

    fun toggleFavorite(course: Course) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.toggleFavorite(course)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun toggleSort() {
        val currentSortType = _uiState.value.sortType
        val nextSortType = when (currentSortType) {
            SortType.NONE -> SortType.BY_DATE
            SortType.BY_DATE -> SortType.BY_RATING
            SortType.BY_RATING -> SortType.BY_PRICE
            SortType.BY_PRICE -> SortType.NONE
        }

        _uiState.value = _uiState.value.copy(sortType = nextSortType)
        applySorting(nextSortType)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun applySorting(sortType: SortType) {
        val sorted = when (sortType) {
            SortType.BY_DATE -> originalFavorites.sortedByDescending { it.publishDate.toLocalDate() }
            SortType.BY_RATING -> originalFavorites.sortedByDescending { it.rate.toFloatOrNull() ?: 0f }
            SortType.BY_PRICE -> originalFavorites.sortedByDescending {
                it.price.replace(" ", "").toIntOrNull() ?: 0
            }
            SortType.NONE -> originalFavorites
        }

        _uiState.value = _uiState.value.copy(favoriteCourses = sorted)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun String.toLocalDate(): LocalDate {
        return try {
            LocalDate.parse(this, DateTimeFormatter.ISO_LOCAL_DATE)
        } catch (e: Exception) {
            LocalDate.MIN
        }
    }
}

data class FavoritesUiState(
    val favoriteCourses: List<Course> = emptyList(),
    val isEmpty: Boolean = true,
    val sortType: SortType = SortType.NONE
)

enum class SortType {
    NONE,
    BY_DATE,
    BY_RATING,
    BY_PRICE
}
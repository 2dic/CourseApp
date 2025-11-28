package com.example.features.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.common.Result
import com.example.domain.model.Course
import com.example.domain.repository.CourseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: CourseRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadCourses()
        loadFavoriteIds()
    }

    fun loadCourses() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(loadingState = Result.Loading)
            when (val result = repository.getCourses()) {
                is Result.Success -> {
                    _uiState.value = _uiState.value.copy(
                        courses = result.data,
                        filteredCourses = result.data,
                        loadingState = Result.Success(Unit)
                    )
                }
                is Result.Error -> {
                    _uiState.value = _uiState.value.copy(
                        loadingState = Result.Error(result.message)
                    )
                }
                else -> {}
            }
        }
    }
    private fun loadFavoriteIds() {
        viewModelScope.launch {
            // Можно доба Flow для отслеживания изменений избранного
            // Пока просто перезагружаем при каждом действии
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun toggleFavorite(course: Course) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.toggleFavorite(course)

            // Получаем актуальные ID избранных курсов
            val favoriteIds = repository.getFavoriteIds()

            // Обновляем локальное состояние
            val updatedCourses = _uiState.value.courses.map {
                it.copy(hasLike = favoriteIds.contains(it.id))
            }
            _uiState.value = _uiState.value.copy(
                courses = updatedCourses,
                filteredCourses = if (_uiState.value.isSorted) {
                    updatedCourses.sortedByDescending { it.publishDate.toLocalDate() }
                } else {
                    updatedCourses
                }
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun toggleSort() {
        val currentCourses = _uiState.value.filteredCourses
        val isSorted = !_uiState.value.isSorted

        val sortedCourses = if (isSorted) {
            currentCourses.sortedByDescending { it.publishDate.toLocalDate() }
        } else {
            _uiState.value.courses
        }

        _uiState.value = _uiState.value.copy(
            filteredCourses = sortedCourses,
            isSorted = isSorted
        )
    }

    fun refreshCourses() {
        loadCourses()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun String.toLocalDate(): LocalDate {
        return LocalDate.parse(this, DateTimeFormatter.ISO_LOCAL_DATE)
    }
}

data class HomeUiState(
    val courses: List<Course> = emptyList(),
    val filteredCourses: List<Course> = emptyList(),
    val loadingState: Result<*> = Result.Loading,
    val isSorted: Boolean = false
)
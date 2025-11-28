package com.example.domain.repository

import com.example.core.common.Result
import com.example.domain.model.Course
import kotlinx.coroutines.flow.Flow

interface CourseRepository {
    suspend fun getCourses(): Result<List<Course>>
    suspend fun toggleFavorite(course: Course)
    fun getFavoriteCourses(): Flow<List<Course>>
    suspend fun getFavoriteIds(): List<Int>
}
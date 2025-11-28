package com.example.data.repository

import com.example.core.common.Result
import com.example.core.database.FavoriteCourse
import com.example.core.database.FavoriteDao
import com.example.core.network.ApiService
import com.example.domain.model.Course
import com.example.domain.repository.CourseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CourseRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val favoriteDao: FavoriteDao
) : CourseRepository {

    override suspend fun getCourses(): Result<List<Course>> {
        return try {
            val response = apiService.getCourses()
            val favoriteIds = getFavoriteIds()

            val coursesWithFavorites = response.courses.map { course ->
                course.copy(hasLike = favoriteIds.contains(course.id))
            }

            Result.Success(coursesWithFavorites)
        } catch (e: Exception) {
            Result.Error("Проблемы с интернетом")
        }
    }

    override suspend fun toggleFavorite(course: Course) {
        val favoriteCourse = FavoriteCourse(
            id = course.id,
            title = course.title,
            text = course.text,
            price = course.price,
            rate = course.rate,
            startDate = course.startDate,
            publishDate = course.publishDate
        )

        if (course.hasLike) {
            favoriteDao.removeFromFavorites(favoriteCourse)
        } else {
            favoriteDao.addToFavorites(favoriteCourse)
        }
    }

    override fun getFavoriteCourses(): Flow<List<Course>> {
        return favoriteDao.getAllFavorites().map { favorites ->
            favorites.map { favorite ->
                Course(
                    id = favorite.id,
                    title = favorite.title,
                    text = favorite.text,
                    price = favorite.price,
                    rate = favorite.rate,
                    startDate = favorite.startDate,
                    hasLike = true,
                    publishDate = favorite.publishDate
                )
            }
        }
    }

    override suspend fun getFavoriteIds(): List<Int> {
        return favoriteDao.getFavoriteIds().first()
    }
}
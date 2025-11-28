package com.example.core.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Delete
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM favorite_courses")
    fun getAllFavorites(): Flow<List<FavoriteCourse>>

    @Query("SELECT * FROM favorite_courses WHERE id = :courseId")
    fun getFavorite(courseId: Int): Flow<FavoriteCourse?>

    @Insert
    fun addToFavorites(course: FavoriteCourse)

    @Delete
    fun removeFromFavorites(course: FavoriteCourse)

    @Query("SELECT id FROM favorite_courses")
    fun getFavoriteIds(): Flow<List<Int>>
}
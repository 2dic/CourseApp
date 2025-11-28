package com.example.coursesapp.di

import android.content.Context
import com.example.core.database.CourseDatabase
import com.example.core.database.FavoriteDao
import com.example.core.network.ApiService
import com.example.core.network.RetrofitInstance
import com.example.domain.repository.CourseRepository
import com.example.data.repository.CourseRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideApiService(): ApiService {
        return RetrofitInstance().api
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): CourseDatabase {
        return CourseDatabase.getInstance(context)
    }

    @Provides
    @Singleton
    fun provideFavoriteDao(database: CourseDatabase): FavoriteDao {
        return database.favoriteDao()
    }

    @Provides
    @Singleton
    fun provideCourseRepository(
        apiService: ApiService,
        favoriteDao: FavoriteDao
    ): CourseRepository {
        return CourseRepositoryImpl(apiService, favoriteDao)
    }
}
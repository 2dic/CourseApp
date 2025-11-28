package com.example.core.database;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\bg\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\'J\u0014\u0010\u0006\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\b0\u0007H\'J\u0018\u0010\t\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00050\u00072\u0006\u0010\n\u001a\u00020\u000bH\'J\u0014\u0010\f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000b0\b0\u0007H\'J\u0010\u0010\r\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\'\u00a8\u0006\u000e"}, d2 = {"Lcom/example/core/database/FavoriteDao;", "", "addToFavorites", "", "course", "Lcom/example/core/database/FavoriteCourse;", "getAllFavorites", "Lkotlinx/coroutines/flow/Flow;", "", "getFavorite", "courseId", "", "getFavoriteIds", "removeFromFavorites", "database_debug"})
@androidx.room.Dao()
public abstract interface FavoriteDao {
    
    @androidx.room.Query(value = "SELECT * FROM favorite_courses")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.example.core.database.FavoriteCourse>> getAllFavorites();
    
    @androidx.room.Query(value = "SELECT * FROM favorite_courses WHERE id = :courseId")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<com.example.core.database.FavoriteCourse> getFavorite(int courseId);
    
    @androidx.room.Insert()
    public abstract void addToFavorites(@org.jetbrains.annotations.NotNull()
    com.example.core.database.FavoriteCourse course);
    
    @androidx.room.Delete()
    public abstract void removeFromFavorites(@org.jetbrains.annotations.NotNull()
    com.example.core.database.FavoriteCourse course);
    
    @androidx.room.Query(value = "SELECT id FROM favorite_courses")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<java.lang.Integer>> getFavoriteIds();
}
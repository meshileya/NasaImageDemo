package com.nasa.demo.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nasa.demo.data.model.NasaImageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NasaImageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImage(image: NasaImageEntity)

    @Delete
    suspend fun removeFavourite(image: NasaImageEntity)

    @Query("SELECT * FROM images WHERE isFavorite = 1")
    fun getFavoriteImages(): Flow<List<NasaImageEntity>>
}
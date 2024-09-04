package com.nasa.demo.data.repository

import androidx.paging.PagingData
import com.nasa.demo.data.model.NasaImage
import com.nasa.demo.data.model.NasaImageEntity
import kotlinx.coroutines.flow.Flow

interface NasaImageRepository {
    suspend fun getNasaImages(): Flow<PagingData<NasaImage>>
    suspend fun toggleFavoriteImage(image: NasaImageEntity)
    fun getFavoriteImages(): Flow<List<NasaImageEntity>>
}